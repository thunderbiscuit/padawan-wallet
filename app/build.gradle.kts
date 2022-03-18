import com.android.build.api.variant.FilterConfiguration.FilterType.ABI

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

val faucetUrl: String by project
val faucetUsername: String by project
val faucetPassword: String by project

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"
    ndkVersion = "21.4.7075529"

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.goldenraven.padawanwallet"
        minSdk = 26
        targetSdk = 30
        versionCode = 8
        versionName = "v0.8.0-SNAPSHOT"
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
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.viewpager:viewpager:1.0.0")

    // bitcoin
    implementation("org.bitcoindevkit:bdk-android:0.5.1")

    // Ktor
    implementation("io.ktor:ktor-client-cio:1.6.7")
    implementation("io.ktor:ktor-client-auth:1.6.7")

    // QR codes
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.github.yuriy-budiyev:code-scanner:2.1.1")

    // Room
    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    // testing
    testImplementation("junit:junit:4.13.2")
}

val abiCodes = mapOf("armeabi-v7a" to 1, "x86" to 2, "x86_64" to 3, "arm64-v8a" to 4)

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
                output.versionCode.set(baseAbiCode * 1000 + (output.versionCode.get() ?: 0))
            }
        }
    }
}
