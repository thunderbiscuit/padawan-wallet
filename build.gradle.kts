plugins {
    // these ensure we use the same plugin versions in all sub-modules
    // TODO: Look into understanding better the apply(false) part
    id("com.android.application").version("8.2.0-rc03").apply(false)
    id("com.android.library").version("8.2.0-rc03").apply(false)
    id("org.jetbrains.kotlin.android").version("1.9.20").apply(false)
    id("org.jetbrains.kotlin.multiplatform").version("1.9.20").apply(false)
    id("com.google.devtools.ksp").version("1.9.20-1.0.14").apply(false)
}
