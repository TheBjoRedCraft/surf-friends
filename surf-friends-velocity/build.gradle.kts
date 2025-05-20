plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.surf.friends.velocity.SurfFriendsPlugin"
    name = "SurfFriends"
    id = "surf-friends"
    authors = listOf("red")
    description = "Surf Friends Proxy instance."
    version = "${rootProject.version}"

    pluginDependencies {
        register("commandapi")
    }
}

dependencies {
    api(project(":surf-friends-core"))
    runtimeOnly(project(":surf-friends-fallback"))
}