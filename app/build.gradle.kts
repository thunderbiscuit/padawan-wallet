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
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"
    ndkVersion = "21.4.7075529"

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.goldenraven.padawanwallet"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "v0.6.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "FAUCET_URL", faucetUrl)
        buildConfigField("String", "FAUCET_USERNAME", faucetUsername)
        buildConfigField("String", "FAUCET_PASSWORD", faucetPassword)
    }

    sourceSets {
        getByName("main").res.setSrcDirs(setOf(
            file("src/main/res/layouts/intro"),
            file("src/main/res/layouts/home"),
            file("src/main/res/layouts/wallet"),
            file("src/main/res/layouts/tutorials"),
            file("src/main/res/layouts/drawer"),
            file("src/main/res")
        ))
    }

    buildTypes {
        getByName("debug") {
            // applicationIdSuffix ".debug"
            debuggable(true)
        }
        getByName("release") {
            // shrinkResources(true)
            // minifyEnabled(true)
            debuggable(true)
            proguardFiles = mutableListOf(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
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
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.viewpager:viewpager:1.0.0")

    // bitcoin
    implementation("org.bitcoindevkit.bdkjni:bdk-jni-debug:0.2.1-dev")

    // Ktor
    implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
    implementation("io.ktor:ktor-client-auth:${Versions.ktor}")

    // logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    // QR codes
    implementation("androidmads.library.qrgenearator:QRGenearator:1.0.4")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.budiyev.android:code-scanner:2.1.0")

    // Room
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")

    // testing
    testImplementation("junit:junit:4.+")
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
