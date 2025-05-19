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

class FriendAddCommand(commandName: String): CommandAPICommand(commandName) {
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

                val friendShip = friendService.getFriendShip(player.uniqueId, targetUuid)

                if(friendShip != null) {
                    player.uniqueId.sendText {
                        error("Du bist bereits mit $target befreundet.")
                    }
                    return@launch
                }

                val friendRequest = friendService.getFriendRequest(player.uniqueId, targetUuid)

                if(friendRequest != null) {
                    player.uniqueId.sendText {
                        error("Du hast bereits eine Freundschaftsanfrage an $target gesendet.")
                    }
                    return@launch
                }

                friendService.sendFriendRequest(player.uniqueId, targetUuid)

                player.uniqueId.sendText {
                    info("Du hast eine Freundschaftsanfrage an ")
                    variableValue(target)
                    info(" gesendet.")
                    //TODO: Add revoke button
                }

                targetUuid.sendText {
                    info("Du hast eine Freundschaftsanfrage von ")
                    variableValue(player.username)
                    info(" erhalten.")
                    //TODO: Add accept and decline buttons
                }
            }
        }
    }
}