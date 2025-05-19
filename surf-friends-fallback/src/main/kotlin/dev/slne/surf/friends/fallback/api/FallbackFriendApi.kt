package dev.slne.surf.friends.fallback.api

import com.google.auto.service.AutoService
import dev.slne.surf.friends.api.SurfFriendsApi
import dev.slne.surf.friends.api.model.FriendRequest
import dev.slne.surf.friends.api.model.FriendShip
import dev.slne.surf.friends.core.service.friendService
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.util.Services
import java.util.UUID

@AutoService(SurfFriendsApi::class)
class FallbackFriendApi: SurfFriendsApi, Services.Fallback {
    override suspend fun createFriendShip(
        uuid: UUID,
        friend: UUID
    ): FriendShip {
        return friendService.createFriendShip(uuid, friend)
    }

    override suspend fun removeFriendShip(uuid: UUID, friend: UUID) {
        friendService.removeFriendShip(uuid, friend)
    }

    override suspend fun getFriendShips(uuid: UUID): ObjectSet<FriendShip> {
        return friendService.getFriendShips(uuid)
    }

    override suspend fun sendFriendRequest(
        sender: UUID,
        receiver: UUID
    ): FriendRequest {
        return friendService.sendFriendRequest(sender, receiver)
    }

    override suspend fun acceptFriendRequest(sender: UUID, receiver: UUID) {
        friendService.acceptFriendRequest(sender, receiver)
    }

    override suspend fun declineFriendRequest(sender: UUID, receiver: UUID) {
        friendService.declineFriendRequest(sender, receiver)
    }

    override suspend fun revokeFriendRequest(sender: UUID, receiver: UUID) {
        friendService.revokeFriendRequest(sender, receiver)
    }

    override suspend fun getSentFriendRequests(uuid: UUID): ObjectSet<FriendRequest> {
        return friendService.getSentFriendRequests(uuid)
    }

    override suspend fun getReceivedFriendRequests(uuid: UUID): ObjectSet<FriendRequest> {
        return friendService.getReceivedFriendRequests(uuid)
    }

    override suspend fun toggleAnnouncements(uuid: UUID): Boolean {
        return friendService.toggleAnnouncements(uuid)
    }

    override suspend fun toggleSounds(uuid: UUID): Boolean {
        return friendService.toggleSounds(uuid)
    }

    override suspend fun areFriends(
        uuid: UUID,
        friend: UUID
    ): FriendShip? {
        return friendService.areFriends(uuid, friend)
    }
}