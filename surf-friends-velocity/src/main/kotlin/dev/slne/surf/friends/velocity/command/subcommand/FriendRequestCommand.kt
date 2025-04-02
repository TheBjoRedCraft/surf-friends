package dev.slne.surf.friends.velocity.command.subcommand

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand

import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.velocity.command.argument.friendArgument

import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.util.toFriendUser
import kotlinx.coroutines.Deferred

class FriendRequestCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        friendArgument("friend")

        playerExecutor { player, args ->
            val friend: Deferred<FriendUser> by args

            container.launch {
                player.toFriendUser().addFriend(friend.await())
            }
        }
    }
}