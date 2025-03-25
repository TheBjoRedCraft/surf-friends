package dev.slne.surf.friends.velocity.command

import dev.jorel.commandapi.CommandAPICommand

class FriendCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        withPermission("surf.friends.command.friend")
        withAliases("f")
    }
}