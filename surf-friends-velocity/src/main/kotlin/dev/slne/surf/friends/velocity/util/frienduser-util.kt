package dev.slne.surf.friends.velocity.util

import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.core.databaseService

fun FriendData.edit(block: FriendData.() -> Unit) {
    this.block()
    databaseService.updateData(this)
}