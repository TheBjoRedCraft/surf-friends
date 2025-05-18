plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    api(project(":surf-friends-core"))
    compileOnly(libs.surf.database)
}