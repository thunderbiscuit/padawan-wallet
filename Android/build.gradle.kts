// All versions of the android, multiplatform, serialization, and compose plugins should be the same

plugins {
    id("com.android.application").version("8.9.1").apply(false)
    id("com.android.library").version("8.9.1").apply(false)
    id("org.jetbrains.kotlin.android").version("2.3.0").apply(false)
    id("org.jetbrains.kotlin.plugin.serialization").version("2.3.0").apply(false)
    id("org.jetbrains.kotlin.plugin.compose").version("2.3.0").apply(false)
}
