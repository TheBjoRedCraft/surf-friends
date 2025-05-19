plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    compileOnly(libs.velocity.api)

    api(project(":surf-friends-core"))
    runtimeOnly(project(":surf-friends-fallback"))
}
