package dev.slne.surf.friends.velocity.command.subcommand

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.database.user
import dev.slne.surf.friends.velocity.util.sendText
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

class FriendListCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        playerExecutor { player, args ->
            container.launch {
                val user = user(player.uniqueId) ?: return@launch

                user.sendText(buildText {
                    primary("Deine Freunde: ")
                    secondary(user.friends.joinToString(", ") { it.friendData.username})
                })
            }
        }
    }
}