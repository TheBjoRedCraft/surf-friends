package dev.slne.surf.friends.core.service

import dev.slne.surf.friends.api.model.FriendRequest
import dev.slne.surf.friends.api.model.FriendShip
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

/**
 * Service interface for managing friendships and related actions.
 */
interface FriendService {
    /**
     * Creates a friendship between two users.
     *
     * @param uuid The UUID of the first user.
     * @param friend The UUID of the friend.
     * @return The created FriendShip object.
     */
    suspend fun createFriendShip(uuid: UUID, friend: UUID): FriendShip

    /**
     * Removes a friendship between two users.
     *
     * @param uuid The UUID of the first user.
     * @param friend The UUID of the friend.
     */
    suspend fun removeFriendShip(uuid: UUID, friend: UUID)

    suspend fun getFriendShip(playerA: UUID, playerB: UUID): FriendShip?
    suspend fun areFriends(uuid: UUID, friend: UUID): FriendShip?

    /**
     * Retrieves all friendships of a user.
     *
     * @param uuid The UUID of the user.
     * @return A set of FriendShips representing the user's friends.
     */
    suspend fun getFriendShips(uuid: UUID): ObjectSet<FriendShip>

    /**
     * Sends a friend request from one user to another.
     *
     * @param sender The UUID of the sender.
     * @param receiver The UUID of the receiver.
     * @return The created FriendRequest object.
     */
    suspend fun sendFriendRequest(sender: UUID, receiver: UUID): FriendRequest

    /**
     * Accepts a friend request.
     *
     * @param sender The UUID of the sender of the request.
     * @param receiver The UUID of the receiver of the request.
     */
    suspend fun acceptFriendRequest(sender: UUID, receiver: UUID)

    /**
     * Declines a friend request.
     *
     * @param sender The UUID of the sender of the request.
     * @param receiver The UUID of the receiver of the request.
     */
    suspend fun declineFriendRequest(sender: UUID, receiver: UUID)

    /**
     * Revokes a sent friend request.
     *
     * @param sender The UUID of the sender of the request.
     * @param receiver The UUID of the receiver of the request.
     */
    suspend fun revokeFriendRequest(sender: UUID, receiver: UUID)

    /**
     * Retrieves all sent friend requests of a user.
     *
     * @param uuid The UUID of the user.
     * @return A set of FriendRequests representing the recipients of the sent requests.
     */
    suspend fun getSentFriendRequests(uuid: UUID): ObjectSet<FriendRequest>

    /**
     * Retrieves all received friend requests of a user.
     *
     * @param uuid The UUID of the user.
     * @return A set of FriendRequests representing the senders of the received requests.
     */
    suspend fun getReceivedFriendRequests(uuid: UUID): ObjectSet<FriendRequest>

    suspend fun getFriendRequest(sender: UUID, target: UUID): FriendRequest?

    /**
     * Toggles announcements for a user.
     *
     * @param uuid The UUID of the user.
     * @return The new status of announcements (true if enabled, false if disabled).
     */
    suspend fun toggleAnnouncements(uuid: UUID): Boolean

    /**
     * Toggles sounds for a user.
     *
     * @param uuid The UUID of the user.
     * @return The new status of sounds (true if enabled, false if disabled).
     */
    suspend fun toggleSounds(uuid: UUID): Boolean

    companion object {
        val INSTANCE = requiredService<FriendService>()
    }
}

/**
 * Extension function to get the friend service instance.
 */
val friendService get() = FriendService.INSTANCE