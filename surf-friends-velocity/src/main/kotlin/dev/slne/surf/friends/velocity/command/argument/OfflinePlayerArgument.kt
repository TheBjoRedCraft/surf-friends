package dev.slne.surf.friends.velocity.command.argument

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext

import com.velocitypowered.api.command.CommandSource

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CommandAPIArgumentType
import dev.jorel.commandapi.executors.CommandArguments

import dev.thebjoredcraft.offlinevelocity.api.`object`.User
import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfflinePlayerArgument(nodeName: String): Argument<User>(nodeName, StringArgumentType.string()) {
    override fun getPrimitiveType(): Class<User> {
        return User::class.java
    }

    override fun getArgumentType(): CommandAPIArgumentType {
        return CommandAPIArgumentType.PRIMITIVE_STRING
    }

    override fun <Source : Any?> parseArgument(
        cmdCtx: CommandContext<Source>,
        key: String,
        previousArgs: CommandArguments,
    ): User {
        val raw = StringArgumentType.getString(cmdCtx, key)

        return withContext(Dispatchers.IO) {
            val user = offlineVelocityApi.getUser(raw) ?: throw CommandAPI.failWithString("Der Spieler $raw wurde nicht gefunden.")

            return@withContext user
        }
    }

    override fun replaceSuggestions(suggestions: ArgumentSuggestions<CommandSource>): Argument<User> {
        return
    }
}