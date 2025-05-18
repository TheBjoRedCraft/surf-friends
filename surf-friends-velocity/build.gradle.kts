plugins {
    kotlin("kapt")
    id("dev.slne.surf.surfapi.gradle.velocity")
    `maven-publish`
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    compileOnly(libs.velocity.api)
    compileOnly(libs.surf.database)

    implementation(libs.caffeine.coroutines)
    implementation(libs.commandapi.velocity)
}

kotlin {
    jvmToolchain(21)
}