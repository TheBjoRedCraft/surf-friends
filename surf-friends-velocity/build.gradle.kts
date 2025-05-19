plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
    kotlin("kapt")
}

dependencies {
    kapt(libs.velocity.api)

    api(project(":surf-friends-core"))
    runtimeOnly(project(":surf-friends-fallback"))
}
