plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("kapt") version "2.1.10" apply false
    `maven-publish`
}

allprojects {
    group = "dev.slne"
    version = "6.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.4+")
    }
}