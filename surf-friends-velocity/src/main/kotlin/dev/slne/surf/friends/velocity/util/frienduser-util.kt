package dev.slne.surf.friends.velocity.util

import com.velocitypowered.api.proxy.Player
import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.databaseService
import dev.slne.surf.friends.velocity.user.VelocityFriendUser

import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi

fun FriendData.edit(block: FriendData.() -> Unit) {
    this.block()
    databaseService.updateData(this)
}

fun FriendData.toFriendUser(): FriendUser {
    return VelocityFriendUser(this)
}

suspend fun Player.toFriendUser(): FriendUser {
    return databaseService.getData(this.uniqueId).toFriendUser()
}

suspend fun String.toFriendUser(): FriendUser? {
    val targetUser = offlineVelocityApi.getUser(this) ?: return null
    return databaseService.getData(targetUser.uuid).toFriendUser()
}