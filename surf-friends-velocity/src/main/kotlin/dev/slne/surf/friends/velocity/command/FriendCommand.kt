package dev.slne.surf.friends.velocity.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.friends.velocity.command.subcommand.FriendAddCommand
import dev.slne.surf.friends.velocity.command.subcommand.FriendListCommand
import dev.slne.surf.friends.velocity.command.subcommand.FriendRemoveCommand
import dev.slne.surf.friends.velocity.command.subcommand.FriendRequestAcceptCommand
import dev.slne.surf.friends.velocity.command.subcommand.FriendRequestDeclineCommand
import dev.slne.surf.friends.velocity.command.subcommand.FriendRequestRevokeCommand

class FriendCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        withPermission("surf.friends.command.friend")
        withAliases("f")

        subcommand(FriendAddCommand("add"))
        subcommand(FriendRemoveCommand("remove"))
        subcommand(FriendRequestAcceptCommand("accept"))
        subcommand(FriendRequestDeclineCommand("decline"))
        subcommand(FriendListCommand("list"))
        subcommand(FriendRequestRevokeCommand("revoke"))
    }
}