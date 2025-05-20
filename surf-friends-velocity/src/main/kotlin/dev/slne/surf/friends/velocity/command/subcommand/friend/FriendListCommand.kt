package dev.slne.surf.friends.velocity.command.subcommand.friend

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.friends.core.service.friendService
import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.util.FriendPermissionRegistry
import dev.slne.surf.friends.velocity.util.sendText

class FriendListCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        withPermission(FriendPermissionRegistry.COMMAND_FRIEND_LIST)
        playerExecutor { player, args ->
            container.launch {
                val friendList = friendService.getFriendships(player.uniqueId)

                if(friendList.isEmpty()) {
                    player.uniqueId.sendText {
                        error("Du hast keine Freunde.")
                    }
                    return@launch
                }

                //TODO: Add PageableMessageBuilder from surf-api?
            }
        }
    }
}