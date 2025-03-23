package dev.slne.surf.friends.api

import dev.slne.surf.friends.api.user.FriendUser

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

    /**
     * Adds a friend to the player's friend list.
     *
     * @param player The user adding a friend.
     * @param friend The user being added as a friend.
     */
    suspend fun addFriend(player: FriendUser, friend: FriendUser)

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
     */
    suspend fun getFriends(player: FriendUser)

    /**
     * Toggles the announcement settings for the specified player.
     *
     * @param player The user toggling announcements.
     */
    suspend fun toggleAnnouncements(player: FriendUser)

    /**
     * Toggles the announcement sound settings for the specified player.
     *
     * @param player The user toggling announcement sounds.
     */
    suspend fun toggleAnnouncementSounds(player: FriendUser)
}