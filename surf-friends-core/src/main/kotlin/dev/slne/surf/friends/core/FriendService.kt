package dev.slne.surf.friends.core

import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

interface FriendService {
    suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser)
    suspend fun revokeFriendRequest(sender: FriendUser, target: FriendUser)

    suspend fun createFriendShip(player: FriendUser, target: FriendUser)
    suspend fun breakFriendShip(player: FriendUser, friend: FriendUser)
    suspend fun getFriends(player: FriendUser): ObjectSet<FriendUser>

    suspend fun toggleAnnouncements(player: FriendUser): Boolean
    suspend fun toggleAnnouncementSounds(player: FriendUser): Boolean

    companion object {
        val INSTANCE: FriendService = requiredService<FriendService>()
    }
}

val friendService get() = FriendService.INSTANCE