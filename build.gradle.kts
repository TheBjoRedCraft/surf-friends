plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("kapt") version "2.1.10" apply false
}

allprojects {
    group = "dev.slne"
    version = "6.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}