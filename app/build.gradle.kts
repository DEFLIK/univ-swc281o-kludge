import org.gradle.kotlin.dsl.implementation
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.wt.vehiclesetting"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.wt.vehiclesetting"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "GIT_HASH", getGitCommitHash())
        }
        debug {
            buildConfigField("String", "GIT_HASH", getGitCommitHash())
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(files("deps/android.car.jar"))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(libs.viewpager2)
    implementation("com.google.code.gson:gson:2.10.1")
}

fun getGitCommitHash(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        val process = Runtime.getRuntime().exec(arrayOf("git", "rev-parse", "--short", "HEAD"))
        process.inputStream.use { input ->
            input.copyTo(stdout)
        }
        process.waitFor()
        "\"${stdout.toString().trim()}\""
    } catch (e: Exception) {
        "\"No git hash\""
    }
}