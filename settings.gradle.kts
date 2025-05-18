plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
include("surf-friends-api")
include("surf-friends-velocity")

include("surf-friends-fallback")
include("surf-friends-core")