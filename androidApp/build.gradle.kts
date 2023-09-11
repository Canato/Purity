import com.canhub.purity.Versions

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.canhub.purity.android"
    compileSdk = Versions.compileSdk
    defaultConfig {
        applicationId = "com.canhub.purity.android"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = System.getProperty("user.name") ?: "dev-local"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:${Versions.composeUi}")
    implementation("androidx.compose.ui:ui-tooling:${Versions.composeUi}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.composeUi}")
    implementation("androidx.compose.foundation:foundation:${Versions.composeUi}")
    implementation("androidx.compose.material:material:${Versions.composeUi}")
    implementation("androidx.activity:activity-compose:${Versions.activityCompose}")
}
