import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

/** Key lookup order: local.properties → Gradle property (-P / gradle.properties) → environment → default. */
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use(::load)
}
fun secret(name: String, default: String = ""): String =
    localProps.getProperty(name)
        ?: providers.gradleProperty(name).orNull
        ?: System.getenv(name)
        ?: default

android {
    namespace = "com.cinecascade.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cinecascade.app"
        minSdk = 26
        targetSdk = 35
        // CI passes the build number so every published APK is newer than the last.
        val buildNumber = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1
        versionCode = buildNumber
        versionName = "0.2.$buildNumber"

        // Keys are never committed: put them in local.properties (or CI secrets).
        buildConfigField("String", "OMDB_API_KEY", "\"${secret("OMDB_API_KEY")}\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"${secret("GEMINI_API_KEY")}\"")
        buildConfigField("String", "GEMINI_MODEL", "\"${secret("GEMINI_MODEL", "gemini-3.5-flash")}\"")
        // Public GitHub repo whose Releases the in-app updater checks.
        buildConfigField("String", "UPDATE_REPO", "\"${System.getenv("GITHUB_REPOSITORY") ?: secret("UPDATE_REPO", "vbromsV/Cinema_Genius_V1")}\"")
    }

    // One fixed debug signing key for every build, so a new APK can install over the old one
    // (Android refuses updates signed with a different key). Uses the standard, public
    // Android debug-keystore passwords; it is not meant for Play Store releases.
    signingConfigs {
        getByName("debug") {
            storeFile = file("cinecascade-debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/versions/9/previous-compilation-data.bin")
    }
}

dependencies {
    implementation(project(":core"))

    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
