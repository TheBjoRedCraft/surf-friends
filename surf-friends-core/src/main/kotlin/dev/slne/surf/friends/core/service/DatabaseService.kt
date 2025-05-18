package dev.slne.surf.friends.core.service

import dev.slne.surf.friends.api.model.FriendRequest
import dev.slne.surf.friends.api.model.FriendShip
import dev.slne.surf.friends.api.util.FriendSettingsPair
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

/**
 * Interface for database operations related to friendships and settings.
 */
interface DatabaseService {
    /**
     * Retrieves the list of friends for a given user.
     *
     * @param uuid The UUID of the user.
     * @return A list of FriendShips representing the user's friends.
     */
    suspend fun getFriends(uuid: UUID): ObjectSet<FriendShip>

    suspend fun getFriendShip(uuid: UUID, friend: UUID): FriendShip?

    suspend fun getFriendRequest(sender: UUID, target: UUID): FriendRequest?

    /**
     * Retrieves the list of sent friend requests for a given user.
     *
     * @param uuid The UUID of the user.
     * @return A list of FriendRequests representing the recipients of the sent requests.
     */
    suspend fun getSentFriendRequests(uuid: UUID): ObjectSet<FriendRequest>

    /**
     * Retrieves the list of received friend requests for a given user.
     *
     * @param uuid The UUID of the user.
     * @return A list of FriendRequests representing the senders of the received requests.
     */
    suspend fun getReceivedFriendRequests(uuid: UUID): ObjectSet<FriendRequest>

    /**
     * Retrieves the friend settings for a given user.
     *
     * @param uuid The UUID of the user.
     * @return A pair representing the user's friend settings.
     */
    suspend fun getFriendSettings(uuid: UUID): FriendSettingsPair

    suspend fun addFriendship(uuid: UUID, friend: UUID): FriendShip
    suspend fun removeFriendship(uuid: UUID, friend: UUID)

    suspend fun addFriendRequest(sender: UUID, receiver: UUID): FriendRequest
    suspend fun removeFriendRequest(sender: UUID, receiver: UUID)
    suspend fun updateFriendSettings(uuid: UUID, pair: FriendSettingsPair): FriendSettingsPair



    companion object {
        val INSTANCE = requiredService<DatabaseService>()
    }
}

/**
 * Extension function to get the friend service instance.
 */
val databaseService get() = DatabaseService.INSTANCE