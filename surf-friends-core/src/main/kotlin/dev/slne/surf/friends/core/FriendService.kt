package dev.slne.surf.friends.core

import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.surfapi.core.api.util.requiredService

interface FriendService {
    suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser)

    suspend fun addFriend(player: FriendUser, friend: FriendUser)
    suspend fun removeFriend(player: FriendUser, friend: FriendUser)
    suspend fun getFriends(player: FriendUser)

    suspend fun toggleAnnouncements(player: FriendUser)
    suspend fun toggleAnnouncementSounds(player: FriendUser)

    companion object {
        val INSTANCE: FriendService = requiredService<FriendService>()
    }
}

val friendService get() = FriendService.INSTANCE