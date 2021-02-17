plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.1"

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.goldenraven.padawanwallet"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.getByName("main") {
        resources.srcDir("src/main/res/layouts/intro")
        resources.srcDir("src/main/res/layouts/home")
        resources.srcDir("src/main/res/layouts/wallet")
        resources.srcDir("src/main/res/layouts/tutorials")
        resources.srcDir("src/main/res/layouts/drawer")
        resources.srcDir("src/main/res")
    }

    buildTypes {
        getByName("debug") {
            // applicationIdSuffix ".debug"
            debuggable(true)
        }
        getByName("release") {
            // minifyEnabled = true
            // shrinkResources = true
            debuggable(true)
            // proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.viewpager:viewpager:1.0.0")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("org.bitcoindevkit.bdkjni:bdk-jni:0.1.0-beta.1")
    implementation("com.jakewharton.timber:timber:4.7.1")
}
