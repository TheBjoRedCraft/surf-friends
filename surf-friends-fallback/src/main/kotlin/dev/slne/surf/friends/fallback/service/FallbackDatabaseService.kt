package dev.slne.surf.friends.fallback.service

import com.google.auto.service.AutoService

import dev.slne.surf.database.DatabaseProvider
import dev.slne.surf.friends.api.model.FriendRequest
import dev.slne.surf.friends.api.model.FriendShip
import dev.slne.surf.friends.api.util.FriendSettingsPair
import dev.slne.surf.friends.core.model.CoreFriendRequest
import dev.slne.surf.friends.core.model.CoreFriendShip
import dev.slne.surf.friends.core.service.DatabaseService
import dev.slne.surf.surfapi.core.api.util.toObjectSet

import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.util.Services

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

import java.nio.file.Path
import java.util.UUID

@AutoService(DatabaseService::class)
class FallbackDatabaseService : DatabaseService, Services.Fallback {
    object FriendShips: Table("friend_ships") {
        val id = integer("id").autoIncrement()
        val userUuid = uuid("user_uuid")
        val friendUuid = uuid("friend_uuid")
        val created_at = long("created_at")

        override val primaryKey = PrimaryKey(id)
    }

    object FriendRequests: Table("friend_requests") {
        val id = integer("id").autoIncrement()
        val senderUuid = uuid("sender_uuid")
        val receiverUuid = uuid("receiver_uuid")
        val send_at = long("created_at")

        override val primaryKey = PrimaryKey(id)
    }

    object FriendSettings: Table("friend_settings") {
        val userUuid = uuid("user_uuid").uniqueIndex()
        var announcementsEnabled = bool("announcements_enabled").default(true)
        var soundsEnabled = bool("sounds_enabled").default(true)

        override val primaryKey = PrimaryKey(userUuid)
    }

    override fun connect(path: Path) {
        println(path)

        DatabaseProvider(path, path).connect()

        transaction {
            SchemaUtils.create(
                FriendShips,
                FriendRequests,
                FriendSettings
            )
        }
    }

    override suspend fun getFriends(
        uuid: UUID
    ) : ObjectSet<FriendShip> {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendShips.select (FriendShips.userUuid eq uuid)
                    .map {
                        CoreFriendShip (
                            userUuid = it[FriendShips.userUuid],
                            friendUuid = it[FriendShips.friendUuid],
                            createdAt = it[FriendShips.created_at]
                        )

                    }
                    .toObjectSet()
            }
        }
    }

    override suspend fun getFriendShip(
        playerA: UUID,
        playerB: UUID
    ): FriendShip? {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendShips.select (
                    (FriendShips.userUuid eq playerA) and (FriendShips.friendUuid eq playerB) or (
                        (FriendShips.userUuid eq playerB) and (FriendShips.friendUuid eq playerA)
                    )
                )
                    .map {
                        CoreFriendShip (
                            userUuid = it[FriendShips.userUuid],
                            friendUuid = it[FriendShips.friendUuid],
                            createdAt = it[FriendShips.created_at]
                        )
                    }
                    .firstOrNull()
            }
        }
    }

    override suspend fun getFriendRequest(
        sender: UUID,
        target: UUID
    ): FriendRequest? {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendRequests.select (
                    (FriendRequests.senderUuid eq sender) and (FriendRequests.receiverUuid eq target)
                )
                    .map {
                        CoreFriendRequest(
                            senderUuid = it[FriendRequests.senderUuid],
                            receiverUuid = it[FriendRequests.receiverUuid],
                            sentAt = it[FriendRequests.send_at]
                        )
                    }
                    .firstOrNull()
            }
        }
    }

    override suspend fun getSentFriendRequests(
        uuid: UUID
    ) : ObjectSet<FriendRequest> {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendRequests.select (FriendRequests.senderUuid eq uuid)
                    .map {
                        CoreFriendRequest(
                            senderUuid = it[FriendRequests.senderUuid],
                            receiverUuid = it[FriendRequests.receiverUuid],
                            sentAt = it[FriendRequests.send_at]
                        )
                    }
                    .toObjectSet()
            }
        }
    }

    override suspend fun getReceivedFriendRequests(
        uuid: UUID
    ) : ObjectSet<FriendRequest> {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendRequests.select (FriendRequests.receiverUuid eq uuid)
                    .map {
                        CoreFriendRequest(
                            senderUuid = it[FriendRequests.senderUuid],
                            receiverUuid = it[FriendRequests.receiverUuid],
                            sentAt = it[FriendRequests.send_at]
                        )
                    }
                    .toObjectSet()
            }
        }
    }

    override suspend fun getFriendSettings(
        uuid: UUID
    ) : FriendSettingsPair {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendSettings.select (FriendSettings.userUuid eq uuid)
                    .map { it.toFriendSettings() }
                    .firstOrNull() ?: FriendSettingsPair()
            }
        }
    }

    override suspend fun addFriendship(
        uuid: UUID,
        friend: UUID
    ) : FriendShip {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                val current: Long = System.currentTimeMillis()

                FriendShips.insert {
                    it[FriendShips.userUuid] = uuid
                    it[FriendShips.friendUuid] = friend
                    it[FriendShips.created_at] = System.currentTimeMillis()
                }

                return@newSuspendedTransaction CoreFriendShip(
                    userUuid = uuid,
                    friendUuid = friend,
                    createdAt = current
                )
            }
        }
    }

    override suspend fun removeFriendship(
        uuid: UUID,
        friend: UUID
    ) {
        withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendShips.deleteWhere {
                    (FriendShips.userUuid eq uuid) and (FriendShips.friendUuid eq friend) or (
                        (FriendShips.userUuid eq friend) and (FriendShips.friendUuid eq uuid)
                    )
                }
            }
        }
    }

    override suspend fun addFriendRequest(
        sender: UUID,
        receiver: UUID
    ) : FriendRequest {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                val current: Long = System.currentTimeMillis()

                FriendRequests.insert {
                    it[FriendRequests.senderUuid] = sender
                    it[FriendRequests.receiverUuid] = receiver
                    it[FriendRequests.send_at] = current
                }

                return@newSuspendedTransaction  CoreFriendRequest(
                    senderUuid = sender,
                    receiverUuid = receiver,
                    sentAt = current
                )
            }
        }
    }

    override suspend fun removeFriendRequest(
        sender: UUID,
        receiver: UUID
    ) {
        withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                FriendRequests.deleteWhere {
                    (FriendRequests.senderUuid eq sender) and (FriendRequests.receiverUuid eq receiver)
                }
            }
        }
    }

    override suspend fun updateFriendSettings (
        uuid: UUID,
        pair: FriendSettingsPair
    ) : FriendSettingsPair {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                if(FriendSettings.select(FriendSettings.userUuid eq uuid).firstOrNull() == null) {
                    FriendSettings.insert {
                        it[FriendSettings.userUuid] = uuid
                        it[FriendSettings.announcementsEnabled] = pair.announcementsEnabled
                        it[FriendSettings.soundsEnabled] = pair.soundsEnabled
                    }
                } else {
                    FriendSettings.update({ FriendSettings.userUuid eq uuid }) {
                        it[FriendSettings.announcementsEnabled] = pair.announcementsEnabled
                        it[FriendSettings.soundsEnabled] = pair.soundsEnabled
                    }
                }

                pair
            }
        }
    }

    fun ResultRow.toFriendSettings() : FriendSettingsPair {
        return FriendSettingsPair(
            announcementsEnabled = this[FriendSettings.announcementsEnabled],
            soundsEnabled = this[FriendSettings.soundsEnabled]
        )
    }
}