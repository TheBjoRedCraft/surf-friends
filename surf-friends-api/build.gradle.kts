plugins {
    id("dev.slne.surf.surfapi.gradle.core")
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.velocity.api)

    api(libs.fast.util)
}

kotlin {
    jvmToolchain(21)
}