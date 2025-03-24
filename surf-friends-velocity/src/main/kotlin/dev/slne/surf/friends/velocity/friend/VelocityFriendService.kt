package dev.slne.surf.friends.velocity.friend

import com.google.auto.service.AutoService
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.FriendService
import dev.slne.surf.friends.core.databaseService
import dev.slne.surf.friends.velocity.util.edit
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.util.Services.Fallback

@AutoService(FriendService::class)
class VelocityFriendService(): FriendService, Fallback {
    override suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = databaseService.getData(target.friendData.uuid);
        val senderData = databaseService.getData(sender.friendData.uuid);

        if (targetData.friendRequests.contains(sender)) {
            //TODO: Send message to sender that they have already sent a friend request to the target
            return
        }

        if(senderData.friendRequests.contains(target)) {
            //TODO: Send message to sender that they have already received a friend request from the target
            return
        }

        if(senderData.friends.contains(target)) {
            //TODO: Send message to sender that they are already friends with the target
            return
        }

        if(targetData.friends.contains(sender)) {
            //TODO: Send message to sender that they are already friends with the target
            return
        }

        targetData.edit {
            friendRequests.add(sender)
        }

        senderData.edit {
            openFriendRequests.add(target)
        }
        //TODO: Send message to target that they have received a friend request from the sender
    }

    override suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = databaseService.getData(target.friendData.uuid);
        val senderData = databaseService.getData(sender.friendData.uuid);

        if(!targetData.friendRequests.contains(sender)) {
            //TODO: Send message to target that they have not received a friend request from the sender
            return
        }

        if(senderData.friends.contains(target)) {
            //TODO: Send message to sender that they are already friends with the target
            return
        }

        targetData.edit {
            friendRequests.remove(sender)
            friends.add(sender)
        }

        senderData.edit {
            openFriendRequests.remove(target)
        }
    }

    override suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = databaseService.getData(target.friendData.uuid);
        val senderData = databaseService.getData(sender.friendData.uuid);

        if(!targetData.friendRequests.contains(sender)) {
            //TODO: Send message to target that they have not received a friend request from the sender
            return
        }

        targetData.edit {
            friendRequests.remove(sender)
        }

        senderData.edit {
            openFriendRequests.remove(target)
        }
    }

    override suspend fun revokeFriendRequest(player: FriendUser, sender: FriendUser) {
        val playerData = databaseService.getData(player.friendData.uuid);
        val senderData = databaseService.getData(sender.friendData.uuid);

        if(!playerData.openFriendRequests.contains(sender)) {
            //TODO: Send message to player that they have not received a friend request from the sender
            return
        }

        playerData.edit {
            openFriendRequests.remove(sender)
        }

        senderData.edit {
            friendRequests.remove(player)
        }
    }

    override suspend fun addFriend(player: FriendUser, friend: FriendUser) {
        val playerData = databaseService.getData(player.friendData.uuid);
        val friendData = databaseService.getData(friend.friendData.uuid);

        if(playerData.friends.contains(friend)) {
            //TODO: Send message to player that they are already friends with the target
            return
        }

        if(friendData.friends.contains(player)) {
            //TODO: Send message to player that they are already friends with the target
            return
        }

        playerData.edit {
            friends.add(friend)
        }
    }

    override suspend fun removeFriend(player: FriendUser, friend: FriendUser) {
        val playerData = databaseService.getData(player.friendData.uuid);
        val friendData = databaseService.getData(friend.friendData.uuid);

        if(!playerData.friends.contains(friend)) {
            //TODO: Send message to player that they are not friends with the target
            return
        }

        playerData.edit {
            friends.remove(friend)
        }

        friendData.edit {
            friends.remove(player)
        }
    }

    override suspend fun getFriends(player: FriendUser): ObjectSet<FriendUser> {
        val playerData = databaseService.getData(player.friendData.uuid);

        return playerData.friends
    }

    override suspend fun toggleAnnouncements(player: FriendUser): Boolean {
        val playerData = databaseService.getData(player.friendData.uuid);

        playerData.edit {
            announcements = !announcements
        }

        return playerData.announcements
    }

    override suspend fun toggleAnnouncementSounds(player: FriendUser): Boolean {
        val playerData = databaseService.getData(player.friendData.uuid);

        playerData.edit {
            announcementSounds = !announcementSounds
        }

        return playerData.announcementSounds
    }
}