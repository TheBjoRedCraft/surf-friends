package dev.slne.surf.friends.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer

import com.google.gson.Gson
import com.google.inject.Inject

import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer

import org.slf4j.Logger
import java.nio.file.Path

@Plugin (
    id = "surf-friends",
    name = "SurfFriends",
    version = "6.0.0-SNAPSHOT",
    description = "Surf Friend System",
    authors = ["SLNE Development"]
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
    }

    companion object {
        lateinit var INSTANCE: SurfFriendsVelocity
        val gson = Gson()
    }
}

val plugin get() = SurfFriendsVelocity.INSTANCE
val gson get() = SurfFriendsVelocity.gson