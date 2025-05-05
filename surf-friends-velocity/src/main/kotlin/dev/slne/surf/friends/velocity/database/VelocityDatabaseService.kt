package dev.slne.surf.friends.velocity.database

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import com.sksamuel.aedile.core.LoadingCache
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.expireAfterWrite
import com.sksamuel.aedile.core.withRemovalListener
import dev.slne.surf.database.DatabaseProvider
import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.DatabaseService
import dev.slne.surf.friends.core.databaseService
import dev.slne.surf.friends.core.type.SearchType
import dev.slne.surf.friends.velocity.SurfFriendsVelocity
import dev.slne.surf.friends.velocity.plugin
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.util.Services.Fallback
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.time.Duration.Companion.minutes

@AutoService(DatabaseService::class)
class VelocityDatabaseService(): DatabaseService, Fallback {
    private val uuidCache: LoadingCache<UUID, FriendData?> = Caffeine.newBuilder()
        .expireAfterWrite(30.minutes)
        .withRemovalListener { pair, data, _ ->
            if (pair != null && data != null) {
                saveData(data as FriendData)
            }
        }
        .asLoadingCache { databaseService.loadData(SearchType.UUID, it.toString()) }

    private val nameCache: LoadingCache<String, FriendData?> = Caffeine.newBuilder()
        .expireAfterWrite(30.minutes)
        .withRemovalListener { pair, data, _ ->
            if (pair != null && data != null) {
                saveData(data as FriendData)
            }
        }
        .asLoadingCache { databaseService.loadData(SearchType.NAME, it) }

    var provider = DatabaseProvider(plugin.dataDirectory, plugin.dataDirectory)

    object Users : Table() {
        val uuid = text("uuid").transform({ UUID.fromString(it) }, { it.toString() })
        val username = text("name")
        val friends = text("friends").transform( {
            SurfFriendsVelocity.gson.fromJson(it, ObjectArraySet<FriendUser>().toObjectSet().javaClass)
        }, {
            SurfFriendsVelocity.gson.toJson(it)
        })
        val friendRequests = text("friendRequests").transform( {
            SurfFriendsVelocity.gson.fromJson(it, ObjectArraySet<FriendUser>().toObjectSet().javaClass)
        }, {
            SurfFriendsVelocity.gson.toJson(it)
        })
        val openFriendRequests = text("openFriendRequests").transform( {
            SurfFriendsVelocity.gson.fromJson(it, ObjectArraySet<FriendUser>().toObjectSet().javaClass)
        }, {
            SurfFriendsVelocity.gson.toJson(it)
        })

        val announcements = bool("announcements")
        val announcementSounds = bool("announcementSounds")

        override val primaryKey = PrimaryKey(uuid)
    }

    override fun connect() {
        provider.connect()

        transaction {
            SchemaUtils.create(Users)
        }
    }

    override suspend fun loadData (
        type: SearchType,
        value: String
    ): FriendData? {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                when(type) {
                    SearchType.NAME -> {
                        Users.select(Users.username eq value).map {
                            FriendData(
                                uuid = it[Users.uuid],
                                username = it[Users.username],
                                friends = it[Users.friends],
                                friendRequests = it[Users.friendRequests],
                                openFriendRequests = it[Users.openFriendRequests],
                                announcements = it[Users.announcements],
                                announcementSounds = it[Users.announcementSounds]
                            )
                        }.firstOrNull()
                    }

                    SearchType.UUID -> {
                        Users.select(Users.uuid eq UUID.fromString(value)).map {
                            FriendData(
                                uuid = it[Users.uuid],
                                username = it[Users.username],
                                friends = it[Users.friends],
                                friendRequests = it[Users.friendRequests],
                                openFriendRequests = it[Users.openFriendRequests],
                                announcements = it[Users.announcements],
                                announcementSounds = it[Users.announcementSounds]
                            )
                        }.firstOrNull()
                    }
                }
            }
        }

    }

    override suspend fun saveData(data: FriendData) {
        withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                Users.replace {
                    it[uuid] = data.uuid
                    it[username] = data.username
                    it[friends] = data.friends
                    it[friendRequests] = data.friendRequests
                    it[openFriendRequests] = data.openFriendRequests
                    it[announcements] = data.announcements
                    it[announcementSounds] = data.announcementSounds
                }
            }
        }
    }

    override suspend fun getData (
        type: SearchType,
        value: String
    ): FriendData? {
        return when(type) {
            SearchType.NAME -> nameCache.get(value)
            SearchType.UUID -> uuidCache.get(UUID.fromString(value))
        }
    }

    override fun updateData(data: FriendData) {
        this.nameCache[data.username] = data
        this.uuidCache[data.uuid] = data
    }

    override fun disconnect() {
        provider.disconnect()
    }
}

suspend fun user(uuid: UUID): FriendData? {
    return databaseService.getData(SearchType.UUID, uuid.toString())
}

suspend fun user(name: String): FriendData? {
    return databaseService.getData(SearchType.NAME, name)
}