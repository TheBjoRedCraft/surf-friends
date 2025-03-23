package dev.slne.surf.friends.api.user

import dev.slne.surf.friends.api.data.FriendData

interface FriendUser {
    val friendData: FriendData

    suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser)

    suspend fun addFriend(player: FriendUser, friend: FriendUser)
    suspend fun removeFriend(player: FriendUser, friend: FriendUser)
    suspend fun getFriends(player: FriendUser)

    suspend fun toggleAnnouncements(player: FriendUser)
    suspend fun toggleAnnouncementSounds(player: FriendUser)
}
