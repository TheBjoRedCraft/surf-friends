package dev.slne.surf.friends.velocity.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.friends.velocity.command.subcommand.request.FriendRequestSendCommand
import dev.slne.surf.friends.velocity.command.subcommand.friend.FriendListCommand
import dev.slne.surf.friends.velocity.command.subcommand.friend.FriendRemoveCommand
import dev.slne.surf.friends.velocity.command.subcommand.request.FriendRequestAcceptCommand
import dev.slne.surf.friends.velocity.command.subcommand.request.FriendRequestDeclineCommand
import dev.slne.surf.friends.velocity.command.subcommand.request.FriendRequestListCommand
import dev.slne.surf.friends.velocity.command.subcommand.request.FriendRequestRevokeCommand

class FriendCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        withPermission("surf.friends.command.friend")
        withAliases("f")

        subcommand(FriendRemoveCommand("remove"))
        subcommand(FriendListCommand("list"))

        subcommand(FriendRequestSendCommand("add"))
        subcommand(FriendRequestAcceptCommand("accept"))
        subcommand(FriendRequestDeclineCommand("decline"))
        subcommand(FriendRequestRevokeCommand("revoke"))
        subcommand(FriendRequestListCommand("requests"))
    }
}