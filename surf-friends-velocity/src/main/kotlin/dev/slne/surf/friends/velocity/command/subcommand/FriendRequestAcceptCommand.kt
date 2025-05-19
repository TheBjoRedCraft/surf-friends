package dev.slne.surf.friends.velocity.command.subcommand

import com.github.shynixn.mccoroutine.velocity.launch

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument

import dev.slne.surf.friends.core.service.friendService
import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.util.sendText
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService

class FriendRequestAcceptCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        stringArgument("player")
        playerExecutor { player, args ->
            container.launch {
                val target: String by args
                val targetUuid = PlayerLookupService.getUuid(target) ?: return@launch run {
                    player.uniqueId.sendText {
                        error("Der Spieler $target wurde nicht gefunden.")
                    }
                }

                val friendRequest = friendService.getFriendRequest(targetUuid, player.uniqueId)

                if(friendRequest == null) {
                    player.uniqueId.sendText {
                        error("Du hast keine Freundschaftsanfrage von $target erhalten.")
                    }
                    return@launch
                }

                val friendShip = friendService.getFriendShip(player.uniqueId, targetUuid)

                if(friendShip != null) {
                    player.uniqueId.sendText {
                        error("Du bist bereits mit $target befreundet.")
                    }
                    return@launch
                }

                friendService.acceptFriendRequest(targetUuid, player.uniqueId)

                player.uniqueId.sendText {
                    info("Du bist nun mit ")
                    variableValue(target)
                    info(" befreundet.")
                }

                targetUuid.sendText {
                    info("Du bist nun mit ")
                    variableValue(player.username)
                    info(" befreundet.")
                }
            }
        }
    }
}