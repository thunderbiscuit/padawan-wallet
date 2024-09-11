rootProject.name = "Padawan Wallet"

include(":app")
include(":padawankmp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        // snapshot repository
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

        // Local Maven (~/.m2/repository/)
        mavenLocal()
    }
}
