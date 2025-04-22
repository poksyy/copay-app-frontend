plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.copay.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.copay.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
// Basic Android dependencies
    implementation(libs.androidx.core.ktx) // Useful extensions for Android APIs.
    implementation(libs.androidx.activity.compose) // Activity integration with Compose.
    implementation(libs.androidx.lifecycle.runtime.ktx) // Extensions to work with the Android lifecycle.

// Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom)) // Composable BOM for Compose.
    implementation(libs.androidx.ui) // Main Jetpack Compose library.
    implementation(libs.androidx.ui.graphics) // Graphics functionalities for Compose.
    implementation(libs.androidx.ui.tooling.preview) // Tools to preview Compose UI in the IDE.
    implementation(libs.material3) // Material Design 3 for Compose.
    implementation(libs.androidx.material.icons.extended) // Extended Material Design icons.
    implementation(libs.androidx.foundation) // Basic Compose components like Layouts and Modifiers.
    implementation(libs.coil.compose) // Library for loading images in Compose.

// ViewModel, LiveData, and Coroutines dependencies
    implementation(libs.kotlinx.coroutines.android) // Allows working with coroutines in Android.
    implementation(libs.androidx.lifecycle.livedata.ktx) // Extensions for LiveData in Compose.
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Integration of ViewModel with Compose.

// Navigation and architecture dependencies
    implementation(libs.androidx.navigation.compose) // Navigation in Compose.
    implementation(libs.dagger.hilt) // Hilt for dependency injection.
    implementation(libs.hilt.compose.navigation) // Hilt integration with Compose navigation.
    kapt(libs.dagger.kapt) // Hilt annotations for dependency injection.


// Retrofit and API communication dependencies
    implementation(libs.retrofit) // Retrofit for making HTTP requests.
    implementation(libs.converter.gson) // Gson converter for Retrofit.
    implementation(libs.logging.interceptor) // OkHttp interceptor for logging HTTP requests.


// Data storage and preferences dependencies
    implementation(libs.androidx.datastore.preferences) // DataStore for storing preferences.

// Unit testing and Android testing dependencies
    testImplementation(libs.junit) // JUnit for unit testing.
    androidTestImplementation(libs.androidx.junit) // JUnit for Android testing.
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing on Android.
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Composable BOM for testing with Compose.
    androidTestImplementation(libs.androidx.ui.test.junit4) // Tools for UI testing with Compose.

// Debugging and development tools dependencies
    debugImplementation(libs.androidx.ui.tooling) // Development tools for working with Compose.
    debugImplementation(libs.androidx.ui.test.manifest) // Testing tools for handling manifests in Compose.

}