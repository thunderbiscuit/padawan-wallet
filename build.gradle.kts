buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        jcenter()
    }
}
