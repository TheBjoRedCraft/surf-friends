package dev.slne.surf.friends.velocity.util

import dev.slne.surf.friends.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import java.util.UUID

fun UUID.sendText(builder: SurfComponentBuilder.() -> Unit) {
    val optionalPlayer = plugin.proxy.getPlayer(this) ?: return
    val player = optionalPlayer.get()

    player.sendMessage(Colors.PREFIX.append(SurfComponentBuilder(builder)))
}