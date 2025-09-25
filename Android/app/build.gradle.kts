import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

val faucetUrl: String by project
val faucetToken: String by project

android {
    compileSdk = 36
    ndkVersion = "21.4.7075529"

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.coyotebitcoin.padawanwallet"
        minSdk = 29
        targetSdk = 36
        // This version code must be updated for each release but the version on the master branch
        // stays at 1.
        versionCode = 1
        versionName = "v0.17.0-SNAPSHOT"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // NOTE: This cannot be configured at the same time as the ndk block when building App Bundles.
        //       When building the App Bundle: comment out the splits block and uncomment the ndk block.
        //       When building the APK:        comment out the ndk block and uncomment the splits block.
        ndk {
            abiFilters += "arm64-v8a"
        }

        resValue(
            type = "string",
            name = "app_name",
            value = "Padawan Wallet",
        )
        buildConfigField("String", "FAUCET_URL", faucetUrl)
        buildConfigField("String", "FAUCET_TOKEN", faucetToken)
    }

    androidResources {
        generateLocaleConfig = true
    }

    // TODO: Look into the R8 aggressive shrinking
    //       https://developer.android.com/build/releases/past-releases/agp-8-0-0-release-notes

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            resValue(
                type = "string",
                name = "app_name",
                value = "Padawan Wallet DEBUG",
            )
            // debuggable(true)
            isDebuggable = true
        }
        getByName("release") {
            // ndk.debugSymbolLevel = "FULL"
            // shrinkResources(true)
            // minifyEnabled(true)
            isDebuggable = false
            // proguardFiles = mutableListOf(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
    }

    // NOTE: This cannot be configured at the same time as the ndk block when building App Bundles.
    //       When building the App Bundle: comment out the splits block and uncomment the ndk block.
    //       When building the APK:        comment out the ndk block and uncomment the splits block.
    // splits {
    //     abi {
    //         isEnable = true
    //         reset()
    //         include("arm64-v8a")
    //         isUniversalApk = false
    //     }
    // }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packaging {
        // for JNA and JNA-platform
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        // for byte-buddy
        resources.excludes.add("META-INF/licenses/ASM")
        resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
        resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
    }

    namespace = "com.coyotebitcoin.padawanwallet"
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

    // Compose
    // Adding the Bill of Materials synchronizes dependencies in the androidx.compose namespace
    // https://developer.android.com/develop/ui/compose/bom/bom-mapping
    // Once declared, you remove the library versions in your dependency declarations
    implementation(platform("androidx.compose:compose-bom:2025.09.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.ui:ui-tooling")

    // Why does this work? As far as I understand it the androidx.activity namespace is not managed by the compose bill of materials
    // Was implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.activity:activity-compose")

    // Navigation
    implementation("androidx.navigation3:navigation3-runtime:1.0.0-alpha09")
    implementation("androidx.navigation3:navigation3-ui:1.0.0-alpha09")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.navigation:navigation-compose:2.9.4")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    // composables.com
    implementation("com.composables:core:1.43.1")

    // Icons
    implementation("com.composables:icons-lucide:1.1.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // Bitcoin
    implementation("org.bitcoindevkit:bdk-android:1.2.0")
    implementation("org.kotlinbitcointools:bip21:0.1.0")

    // Ktor
    implementation("io.ktor:ktor-client-cio:3.3.0")
    implementation("io.ktor:ktor-client-auth:3.3.0")

    // QR codes
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
    implementation("androidx.camera:camera-camera2:1.5.0")
    implementation("androidx.camera:camera-lifecycle:1.5.0")
    implementation("androidx.camera:camera-view:1.5.0")

    // Unit testing
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.13.4")
    testImplementation("org.mockito:mockito-core:5.19.0")

    // Instrumentation testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.9.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.9.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.withType<Test> {
    useJUnitPlatform() // JUnit 5

    testLogging {
        events(PASSED, SKIPPED, FAILED, STANDARD_OUT, STANDARD_ERROR)
        exceptionFormat = FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

// val abiCodes = mapOf("x86_64" to 1, "arm64-v8a" to 2)
// val abiCodes = mapOf("armeabi-v7a" to 1, "x86_64" to 2, "arm64-v8a" to 3)

// androidComponents {
//     onVariants { variant ->
//
//         // Assigns a different version code for each output APK
//         // other than the universal APK.
//         variant.outputs.forEach { output ->
//             val name = output.filters.find { it.filterType == ABI }?.identifier
//
//             // Stores the value of abiCodes that is associated with the ABI for this variant.
//             val baseAbiCode = abiCodes[name]
//             // Because abiCodes.get() returns null for ABIs that are not mapped by ext.abiCodes,
//             // the following code does not override the version code for universal APKs.
//             // However, because we want universal APKs to have the lowest version code,
//             // this outcome is desirable.
//             if (baseAbiCode != null) {
//                 // Assigns the new version code to output.versionCode, which changes the version code
//                 // for only the output APK, not for the variant itself.
//                 output.versionCode.set(baseAbiCode * 1000 + (output.versionCode.get() ?: 0))
//             }
//         }
//     }
// }
