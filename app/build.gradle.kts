plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.navigation.safeargs.kotlin)
}

android {
    namespace = "com.vehicle.selector"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vehicle.selector"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.fragment)

    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.fragment)

    implementation(libs.androidx.recyclerview)

    implementation(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)

    implementation(libs.epoxy)
    implementation(libs.androidx.swiperefreshlayout)
    kapt(libs.epoxy.processor)

    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room)
    api(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.lottie)

    implementation(libs.gson)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.androidx.constraintlayout)

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.ktor.client.okhttp)

    testImplementation(libs.junit)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation("io.mockk:mockk:1.12.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("io.ktor:ktor-client-mock:1.5.0")
}