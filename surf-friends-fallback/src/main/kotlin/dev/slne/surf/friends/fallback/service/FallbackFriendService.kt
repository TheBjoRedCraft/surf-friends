package dev.slne.surf.friends.fallback.service

import com.google.auto.service.AutoService
import dev.slne.surf.friends.api.model.FriendRequest
import dev.slne.surf.friends.api.model.FriendShip
import dev.slne.surf.friends.core.service.FriendService
import dev.slne.surf.friends.core.service.databaseService
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.util.Services
import java.util.UUID

@AutoService(FriendService::class)
class FallbackFriendService : FriendService, Services.Fallback {
    override suspend fun createFriendShip(uuid: UUID, friend: UUID): FriendShip {
        val friendShip = databaseService.getFriendShip(uuid, friend)

        if(friendShip != null) {
            return friendShip
        }

        return databaseService.addFriendship(uuid, friend)
    }

    override suspend fun removeFriendShip(uuid: UUID, friend: UUID) {
        val friendShip = databaseService.getFriendShip(uuid, friend)

        if(friendShip == null) {
            return
        }

        databaseService.removeFriendship(uuid, friend)
    }

    override suspend fun getFriendShip(
        playerA: UUID,
        playerB: UUID
    ): FriendShip? {
        return databaseService.getFriendShip(playerA, playerB)
    }

    override suspend fun areFriends(
        uuid: UUID,
        friend: UUID
    ): FriendShip? {
        return databaseService.getFriendShip(uuid, friend)
    }

    override suspend fun getFriendShips(uuid: UUID): ObjectSet<FriendShip> {
        return databaseService.getFriends(uuid)
    }

    override suspend fun sendFriendRequest(sender: UUID, receiver: UUID): FriendRequest {
        val friendRequest = databaseService.getFriendRequest(sender, receiver)

        if(friendRequest != null) {
            return friendRequest
        }

        return databaseService.addFriendRequest(sender, receiver)
    }

    override suspend fun acceptFriendRequest(sender: UUID, receiver: UUID) {
        val friendRequest = databaseService.getFriendRequest(sender, receiver)

        if(friendRequest == null) {
            return
        }

        databaseService.removeFriendRequest(sender, receiver)
        databaseService.addFriendship(sender, receiver)
    }

    override suspend fun declineFriendRequest(sender: UUID, receiver: UUID) {
        val friendRequest = databaseService.getFriendRequest(sender, receiver)

        if(friendRequest == null) {
            return
        }

        databaseService.removeFriendRequest(sender, receiver)
    }

    override suspend fun revokeFriendRequest(sender: UUID, receiver: UUID) {
        val friendRequest = databaseService.getFriendRequest(sender, receiver)

        if(friendRequest == null) {
            return
        }

        databaseService.removeFriendRequest(sender, receiver)
    }

    override suspend fun getSentFriendRequests(uuid: UUID): ObjectSet<FriendRequest> {
        return databaseService.getSentFriendRequests(uuid)
    }

    override suspend fun getReceivedFriendRequests(uuid: UUID): ObjectSet<FriendRequest> {
        return databaseService.getReceivedFriendRequests(uuid)
    }

    override suspend fun getFriendRequest(
        sender: UUID,
        target: UUID
    ): FriendRequest? {
        return databaseService.getFriendRequest(sender, target)
    }

    override suspend fun toggleAnnouncements(uuid: UUID): Boolean {
        val friendSettings = databaseService.getFriendSettings(uuid)
        val newSettings = friendSettings.copy(announcementsEnabled = !friendSettings.announcementsEnabled)

        return databaseService.updateFriendSettings(uuid, newSettings).announcementsEnabled
    }

    override suspend fun toggleSounds(uuid: UUID): Boolean {
        val friendSettings = databaseService.getFriendSettings(uuid)
        val newSettings = friendSettings.copy(soundsEnabled = !friendSettings.soundsEnabled)

        return databaseService.updateFriendSettings(uuid, newSettings).soundsEnabled
    }
}