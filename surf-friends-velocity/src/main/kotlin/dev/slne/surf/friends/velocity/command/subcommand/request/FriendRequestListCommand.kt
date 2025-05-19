package dev.slne.surf.friends.velocity.command.subcommand.request

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.friends.core.service.friendService
import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.util.sendText

class FriendRequestListCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        playerExecutor { player, args ->
            container.launch {
                val friendRequests = friendService.getReceivedFriendRequests(player.uniqueId)

                if(friendRequests.isEmpty()) {
                    player.uniqueId.sendText {
                        error("Du hast keine Freundschaftsanfragen offen.")
                    }
                    return@launch
                }

                //TODO: Add PageableMessageBuilder from surf-api?
            }
        }
    }
}