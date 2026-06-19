pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Padawan Wallet"

include(":app")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        // snapshot repository
        // maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

        // Local Maven (~/.m2/repository/)
        // mavenLocal()
    }
}
