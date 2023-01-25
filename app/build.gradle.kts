import com.android.build.api.variant.FilterConfiguration.FilterType.ABI
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

val faucetUrl: String by project
val faucetUsername: String by project
val faucetPassword: String by project

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"
    ndkVersion = "21.4.7075529"

    buildFeatures {
        viewBinding = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.goldenraven.padawanwallet"
        minSdk = 26
        targetSdk = 33
        versionCode = 10
        versionName = "v0.10.0-SNAPSHOT"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue(
            type = "string",
            name = "app_name",
            value = "Padawan Wallet",
        )
        buildConfigField("String", "FAUCET_URL", faucetUrl)
        buildConfigField("String", "FAUCET_USERNAME", faucetUsername)
        buildConfigField("String", "FAUCET_PASSWORD", faucetPassword)
    }

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

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "x86", "armeabi-v7a", "x86_64")
            isUniversalApk = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }

    packagingOptions {
        // for JNA and JNA-platform
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        // for byte-buddy
        resources.excludes.add("META-INF/licenses/ASM")
        resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
        resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("com.google.android.material:material:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    // compose
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.animation:animation:1.3.3")
    implementation("androidx.compose.ui:ui-tooling:1.3.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.3.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.23.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("com.google.accompanist:accompanist-pager:0.23.1")

    // bitcoin
    implementation("org.bitcoindevkit:bdk-android:0.26.0")

    // Ktor
    implementation("io.ktor:ktor-client-cio:2.2.1")
    implementation("io.ktor:ktor-client-auth:2.2.1")

    // QR codes
    implementation("com.google.zxing:core:3.4.1")
    implementation("androidx.camera:camera-camera2:1.2.0")
    implementation("androidx.camera:camera-lifecycle:1.2.0")
    implementation("androidx.camera:camera-view:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")

    // Unit testing
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.8.2")
    testImplementation("org.mockito:mockito-core:4.4.0")

    // Instrumentation testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.3")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events(PASSED, SKIPPED, FAILED, STANDARD_OUT, STANDARD_ERROR)
        exceptionFormat = FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

val abiCodes = mapOf("armeabi-v7a" to 1, "x86_64" to 2, "arm64-v8a" to 3)

androidComponents {
    onVariants { variant ->

        // Assigns a different version code for each output APK
        // other than the universal APK.
        variant.outputs.forEach { output ->
            val name = output.filters.find { it.filterType == ABI }?.identifier

            // Stores the value of abiCodes that is associated with the ABI for this variant.
            val baseAbiCode = abiCodes[name]
            // Because abiCodes.get() returns null for ABIs that are not mapped by ext.abiCodes,
            // the following code does not override the version code for universal APKs.
            // However, because we want universal APKs to have the lowest version code,
            // this outcome is desirable.
            if (baseAbiCode != null) {
                // Assigns the new version code to output.versionCode, which changes the version code
                // for only the output APK, not for the variant itself.
                output.versionCode.set(baseAbiCode * 1000 + (output.versionCode.get()  ?: 0))
            }
        }
    }
}
