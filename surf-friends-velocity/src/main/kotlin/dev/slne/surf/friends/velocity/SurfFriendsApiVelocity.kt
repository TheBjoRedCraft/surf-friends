package dev.slne.surf.friends.velocity

import com.google.auto.service.AutoService

import dev.slne.surf.friends.api.SurfFriendsApi
import dev.slne.surf.friends.api.user.FriendUser

import net.kyori.adventure.util.Services.Fallback

@AutoService(SurfFriendsApi::class)
class SurfFriendsApiVelocity(): SurfFriendsApi, Fallback {
    override suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser) {
        
    }

    override suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser) {
        TODO("Not yet implemented")
    }

    override suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser) {
        TODO("Not yet implemented")
    }

    override suspend fun addFriend(player: FriendUser, friend: FriendUser) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFriend(player: FriendUser, friend: FriendUser) {
        TODO("Not yet implemented")
    }

    override suspend fun getFriends(player: FriendUser) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleAnnouncements(player: FriendUser) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleAnnouncementSounds(player: FriendUser) {
        TODO("Not yet implemented")
    }

}