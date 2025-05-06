package dev.slne.surf.friends.velocity.util

import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.databaseService
import dev.slne.surf.friends.velocity.plugin
import dev.slne.surf.friends.velocity.user.VelocityFriendUser
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

import net.kyori.adventure.text.Component

fun FriendData.edit(block: FriendData.() -> Unit) {
    this.block()
    databaseService.updateData(this)
}

fun FriendData.user(): FriendUser {
    return VelocityFriendUser(this)
}


fun FriendData.sendText(text: Component) {
    val player = plugin.proxy.getPlayer(this.uuid)

    if(player.isEmpty) {
        return
    }

    player.get().sendMessage(buildText {
        appendPrefix()
        append(text)
    })
}

fun FriendUser.sendText(text: Component) {
    this.friendData.sendText(text)
}