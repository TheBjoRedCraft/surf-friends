plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":surf-friends-api"))
}

kotlin {
    jvmToolchain(21)
}