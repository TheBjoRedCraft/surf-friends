package dev.slne.surf.friends.velocity.database

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.expireAfterWrite
import com.sksamuel.aedile.core.withRemovalListener
import dev.hsbrysk.caffeine.CoroutineLoadingCache
import dev.hsbrysk.caffeine.buildCoroutine
import dev.slne.surf.database.DatabaseProvider
import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.DatabaseService
import dev.slne.surf.friends.core.databaseService
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
    private val dataCache = Caffeine.newBuilder()
        .expireAfterWrite(20.minutes)
        .withRemovalListener { uuid, data, _ ->
            if (uuid != null && data != null) {
                saveData(data as FriendData)
            }
        }
        .asLoadingCache<UUID, FriendData> { databaseService.loadData(it) }

    object Users : Table() {
        val uuid = text("uuid").transform({ UUID.fromString(it) }, { it.toString() })
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

        val announcements = bool("announcements")
        val announcementSounds = bool("announcementSounds")

        override val primaryKey = PrimaryKey(uuid)
    }

    override fun connect() {
        DatabaseProvider(plugin.dataDirectory, plugin.dataDirectory).connect()

        transaction {
            SchemaUtils.create(Users)
        }
    }

    override suspend fun loadData(uuid: UUID): FriendData {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                val selected = Users.select(Users.uuid eq uuid).firstOrNull() ?: return@newSuspendedTransaction FriendData(uuid)

                return@newSuspendedTransaction FriendData(
                    selected[Users.uuid],
                    selected[Users.friends],
                    selected[Users.friendRequests],
                    selected[Users.announcements],
                    selected[Users.announcementSounds]
                )
            }
        }
    }

    override suspend fun saveData(data: FriendData) {
        withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                Users.replace {
                    it[uuid] = data.uuid
                    it[friends] = data.friends
                    it[friendRequests] = data.friendRequests
                    it[announcements] = data.announcements
                    it[announcementSounds] = data.announcementSounds
                }
            }
        }
    }

    override suspend fun getData(uuid: UUID): FriendData? {
        return null
    }

    override fun disconnect() {

    }
}