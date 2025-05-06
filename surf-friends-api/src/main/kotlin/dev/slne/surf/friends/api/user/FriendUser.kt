package dev.slne.surf.friends.api.user

import dev.slne.surf.friends.api.data.FriendData
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

interface FriendUser {
    val friendData: FriendData

    suspend fun sendFriendRequest(target: FriendUser)
    suspend fun acceptFriendRequest(target: FriendUser)
    suspend fun declineFriendRequest(target: FriendUser)
    suspend fun revokeFriendRequest(target: FriendUser)

    suspend fun addFriend(friend: FriendUser)
    suspend fun removeFriend(friend: FriendUser)
    suspend fun getFriends(): ObjectSet<UUID>

    suspend fun toggleAnnouncements(): Boolean
    suspend fun toggleAnnouncementSounds(): Boolean
}
