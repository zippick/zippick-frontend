plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.zippick"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.zippick"
        minSdk = 24
        targetSdk = 36
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
        viewBinding = true
        // compose = true
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.8.3"
//    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 컴포즈 관련
//    implementation("androidx.compose.ui:ui:1.8.3")
//    implementation("androidx.compose.material3:material3:1.2.1")
//    implementation("androidx.compose.ui:ui-tooling-preview:1.8.3")
//    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation("androidx.compose.runtime:runtime-livedata:1.8.3")
//    implementation("androidx.compose.material:material:1.8.3")
//    implementation("androidx.compose.compiler:compiler:1.8.3")

}