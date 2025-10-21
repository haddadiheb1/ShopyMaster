plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.hanouti.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hanouti.app"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui-text-google-fonts")
    // Image loading for local URIs
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Material Icons Extended (needed for Icons.Filled.Store)
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // App language switching (per-app locales)
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Preferences DataStore for persisting language
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Material Components (Views) for XML theme: Theme.Material3.* and color attrs
    implementation("com.google.android.material:material:1.12.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // CameraX
    val cameraXVersion = "1.3.4"
    implementation("androidx.camera:camera-core:$cameraXVersion")
    implementation("androidx.camera:camera-camera2:$cameraXVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraXVersion")
    implementation("androidx.camera:camera-view:$cameraXVersion")

    // ML Kit Barcode Scanning
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    
    // QR Code generation and scanning
    implementation("com.google.zxing:core:3.5.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    
    // JSON serialization for data export/import
    implementation("com.google.code.gson:gson:2.10.1")

}
