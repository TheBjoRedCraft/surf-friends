package dev.slne.surf.friends.api

import dev.slne.surf.friends.api.user.FriendUser
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

interface SurfFriendsApi {
    /**
     * Sends a friend request from the sender to the target user.
     *
     * @param sender The user sending the friend request.
     * @param target The user receiving the friend request.
     */
    suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser)

    /**
     * Accepts a friend request from the sender to the target user.
     *
     * @param sender The user whose friend request is being accepted.
     * @param target The user accepting the friend request.
     */
    suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser)

    /**
     * Declines a friend request from the sender to the target user.
     *
     * @param sender The user whose friend request is being declined.
     * @param target The user declining the friend request.
     */
    suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser)

    suspend fun revokeFriendRequest(player: FriendUser, sender: FriendUser)

    /**
     * Creates a friendship between the player and the friend.
     *
     * @param player The user adding a friend.
     * @param friend The user being added as a friend.
     */
    suspend fun createFriendShip(player: FriendUser, friend: FriendUser)

    /**
     * Removes a friend from the player's friend list.
     *
     * @param player The user removing a friend.
     * @param friend The user being removed as a friend.
     */
    suspend fun removeFriend(player: FriendUser, friend: FriendUser)

    /**
     * Retrieves the list of friends for the specified player.
     *
     * @param player The user whose friends are being retrieved.
     *
     * @return The list of friends for the specified player.
     */
    suspend fun getFriends(player: FriendUser): ObjectSet<UUID>

    /**
     * Toggles the announcement settings for the specified player.
     *
     * @param player The user toggling announcements.
     * @return The new announcement setting for the player.
     */
    suspend fun toggleAnnouncements(player: FriendUser): Boolean

    /**
     * Toggles the announcement sound settings for the specified player.
     *
     * @param player The user toggling announcement sounds.
     * @return The new announcement sound setting for the player.
     */
    suspend fun toggleAnnouncementSounds(player: FriendUser): Boolean
}