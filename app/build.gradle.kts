plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.darshanassignment2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.darshanassignment2"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // ✅ GSON library for JSON support
    implementation("com.google.code.gson:gson:2.10.1")

    // ✅ Jetpack & Wear libraries from version catalog
    implementation(libs.play.services.wearable)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.wear)
}
