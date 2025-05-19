package dev.slne.surf.friends.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer

import com.google.gson.Gson
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent

import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIVelocityConfig
import dev.slne.surf.friends.velocity.command.FriendCommand

import org.slf4j.Logger
import java.nio.file.Path

@Plugin (
    id = "surf-friends",
    name = "SurfFriends",
    version = "1.7.0-SNAPSHOT",
    description = "Surf Friend System",
    authors = ["red"]
)
class SurfFriendsVelocity
@Inject
constructor (
    val logger: Logger,
    val proxy: ProxyServer,
    @DataDirectory val dataDirectory: Path,
    suspendingPluginContainer: SuspendingPluginContainer
) {
    init {
        suspendingPluginContainer.initialize(this)
        INSTANCE = this

        CommandAPI.onLoad(CommandAPIVelocityConfig(proxy, this))
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        CommandAPI.onEnable()

        FriendCommand("friend").register()
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        CommandAPI.onDisable()
    }

    companion object {
        lateinit var INSTANCE: SurfFriendsVelocity
        val gson = Gson()
    }
}

val container get() = plugin.proxy.pluginManager.getPlugin("surf-friends").get()
val plugin get() = SurfFriendsVelocity.INSTANCE