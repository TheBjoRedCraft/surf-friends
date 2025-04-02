package dev.slne.surf.friends.velocity.command.argument

import com.github.shynixn.mccoroutine.velocity.launch
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.velocitypowered.api.command.CommandSource
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CommandAPIArgumentType
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.velocity.container
import dev.slne.surf.friends.velocity.util.toFriendUser
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

@Suppress("UNCHECKED_CAST")
class FriendArgument(nodeName: String): Argument<Deferred<FriendUser>>(nodeName, StringArgumentType.string()) {
    override fun getPrimitiveType(): Class<Deferred<FriendUser>> {
        return Deferred::class.java as Class<Deferred<FriendUser>>
    }

    override fun getArgumentType(): CommandAPIArgumentType {
        return CommandAPIArgumentType.PRIMITIVE_STRING
    }

    override fun <Source : Any?> parseArgument(cmdCtx: CommandContext<Source>, key: String, previousArgs: CommandArguments): Deferred<FriendUser> {
        val playerName = StringArgumentType.getString(cmdCtx, key)
        val deferrable = CompletableDeferred<FriendUser>()

        container.launch {
            deferrable.complete(playerName.toFriendUser() ?: throw CommandAPI.failWithString("Der Spieler $playerName wurde nicht gefunden."))
        }

        return deferrable
    }

    override fun replaceSuggestions(suggestions: ArgumentSuggestions<CommandSource>): Argument<Deferred<FriendUser>> {

        return this
    }
}

inline fun CommandAPICommand.friendArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(
    FriendArgument(nodeName).setOptional(optional).apply(block)
)