package dev.slne.surf.friends.velocity.command.subcommand.toggle

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand

class FriendToggleCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        withPermission("surf.friends.command.toggle")
        subcommand(FriendAnnouncementsToggleCommand("announcements"))
        subcommand(FriendSoundToggleCommand("sounds"))
    }
}