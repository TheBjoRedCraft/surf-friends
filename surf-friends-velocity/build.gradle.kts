plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.surf.friends.velocity.SurfFriendsPlugin"

    pluginDependencies {
        register("surf-api-velocity")
        register("commandapi")
    }
}

dependencies {
    api(project(":surf-friends-core"))
    runtimeOnly(project(":surf-friends-fallback"))
}
