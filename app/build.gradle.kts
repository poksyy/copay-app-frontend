plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Allows to work with coroutines in the ViewModel.
    implementation (libs.kotlinx.coroutines.android)
    // LiveData support in Compose.
    implementation (libs.androidx.lifecycle.livedata.ktx)
    // Allows to use ViewModels with Compose.
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    // Navigation library.
    implementation (libs.androidx.navigation.compose)
    // Retrofit core library for making HTTP requests and handling API communication.
    implementation(libs.retrofit)
    // Gson converter for Retrofit, enabling automatic JSON serialization and deserialization.
    implementation(libs.converter.gson)
    // OkHttp Logging Interceptor for logging HTTP request and response details, useful for debugging.
    implementation(libs.logging.interceptor)
    // DataStore Preferences for storing key-value pairs in a modern and safe way.
    implementation (libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}