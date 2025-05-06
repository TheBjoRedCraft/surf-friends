package dev.slne.surf.friends.velocity.user

import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.friendService
import it.unimi.dsi.fastutil.objects.ObjectSet

class VelocityFriendUser(override val friendData: FriendData): FriendUser {
    override suspend fun sendFriendRequest(target: FriendUser) {
        friendService.sendFriendRequest(this, target)
    }

    override suspend fun acceptFriendRequest(target: FriendUser) {
        friendService.acceptFriendRequest(this, target)
    }

    override suspend fun declineFriendRequest(target: FriendUser) {
        friendService.declineFriendRequest(this, target)
    }

    override suspend fun revokeFriendRequest(target: FriendUser) {
        friendService.revokeFriendRequest(this, target)
    }

    override suspend fun addFriend(friend: FriendUser) {
        friendService.createFriendShip(this, friend)
    }

    override suspend fun removeFriend(friend: FriendUser) {
        friendService.breakFriendShip(this, friend)
    }

    override suspend fun getFriends(): ObjectSet<FriendUser> {
        return friendService.getFriends(this)
    }

    override suspend fun toggleAnnouncements(): Boolean {
        return friendService.toggleAnnouncements(this)
    }

    override suspend fun toggleAnnouncementSounds(): Boolean {
        return friendService.toggleAnnouncementSounds(this)
    }
}