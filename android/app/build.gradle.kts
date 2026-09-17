plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.smartcontrol.pro"
    compileSdk = 35
    defaultConfig { applicationId = "com.smartcontrol.pro"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "0.1.0" }
    buildFeatures { compose = true; buildConfig = true }
    signingConfigs {
        create("release") {
            val filePath = System.getenv("ANDROID_KEYSTORE_FILE") ?: project.findProperty("ANDROID_KEYSTORE_FILE") as String?
            if (!filePath.isNullOrBlank()) {
                storeFile = file(filePath)
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD") ?: project.findProperty("ANDROID_KEYSTORE_PASSWORD") as String?
                keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: project.findProperty("ANDROID_KEY_ALIAS") as String?
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD") ?: project.findProperty("ANDROID_KEY_PASSWORD") as String?
            }
        }
    }
    buildTypes { release { isMinifyEnabled = false; signingConfig = signingConfigs.getByName("release") } }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}
kotlin { jvmToolchain(17) }

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.compose.ui:ui"); implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3"); implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.8.5"); implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0")); implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
