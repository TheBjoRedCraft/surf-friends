package dev.slne.surf.friends.velocity.command.subcommand

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.database.user
import dev.slne.surf.friends.velocity.plugin
import dev.slne.surf.friends.velocity.util.sendText
import dev.slne.surf.friends.velocity.util.user
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

class FriendRequestAcceptCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        stringArgument("player")
        playerExecutor { player, args ->
            container.launch {
                val user = user(player.uniqueId) ?: return@launch
                val target = user(args.getOrDefaultUnchecked("player", "Unknown")) ?: return@launch run {
                    user.sendText(buildText {
                        error("Der Spieler wurde nicht gefunden.")
                    })
                }

                if (!user.friendRequests.contains(target.uuid)) {
                    user.sendText(buildText {
                        error("Du hast keine Freundschaftsanfrage von ${target.username} erhalten.")
                    })
                    return@launch
                }

                if (user.friends.contains(target.uuid)) {
                    user.sendText(buildText {
                        error("Du bist bereits mit ${target.username} befreundet.")
                    })
                    return@launch
                }

                if (target.friends.contains(user.uuid)) {
                    user.sendText(buildText {
                        error("${target.username} ist bereits dein Freund.")
                    })
                    return@launch
                }

                user.user().acceptFriendRequest(target.user())
            }
        }
    }
}