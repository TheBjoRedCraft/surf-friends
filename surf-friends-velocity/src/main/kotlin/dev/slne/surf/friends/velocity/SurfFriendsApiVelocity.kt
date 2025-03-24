package dev.slne.surf.friends.velocity

import com.google.auto.service.AutoService

import dev.slne.surf.friends.api.SurfFriendsApi
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.friendService
import it.unimi.dsi.fastutil.objects.ObjectSet

import net.kyori.adventure.util.Services.Fallback

@AutoService(SurfFriendsApi::class)
class SurfFriendsApiVelocity(): SurfFriendsApi, Fallback {
    override suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser) {
        friendService.sendFriendRequest(sender, target)
    }

    override suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser) {
        friendService.acceptFriendRequest(sender, target)
    }

    override suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser) {
        friendService.declineFriendRequest(sender, target)
    }

    override suspend fun addFriend(player: FriendUser, friend: FriendUser) {
        friendService.addFriend(player, friend)
    }

    override suspend fun removeFriend(player: FriendUser, friend: FriendUser) {
        friendService.removeFriend(player, friend)
    }

    override suspend fun getFriends(player: FriendUser): ObjectSet<FriendUser> {
        return friendService.getFriends(player)
    }

    override suspend fun toggleAnnouncements(player: FriendUser): Boolean {
        return friendService.toggleAnnouncements(player)
    }

    override suspend fun toggleAnnouncementSounds(player: FriendUser): Boolean {
        return friendService.toggleAnnouncementSounds(player)
    }
}