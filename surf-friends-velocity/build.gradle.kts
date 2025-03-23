plugins {
    kotlin("kapt")
    id("dev.slne.surf.surfapi.gradle.velocity")
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity.api)
    compileOnly(libs.surf.database)

    implementation(libs.caffeine.coroutines)

    api(project(":surf-friends-core"))
}

kotlin {
    jvmToolchain(21)
}