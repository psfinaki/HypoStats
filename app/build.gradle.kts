plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

android {
    namespace = "app.hypostats"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.hypostats"
        minSdk = 26
        targetSdk = 36
        versionCode = 8
        versionName = "0.4.8-dev"
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    signingConfigs {
        if (project.hasProperty("KEYSTORE_FILE")) {
            create("release") {
                storeFile = file(project.property("KEYSTORE_FILE") as String)
                storePassword = project.property("KEYSTORE_PASSWORD") as String
                keyAlias = project.property("KEY_ALIAS") as String
                keyPassword = project.property("KEY_PASSWORD") as String
            }
        }
    }

    buildTypes {
        release {
            if (project.hasProperty("KEYSTORE_FILE")) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("detekt.yml"))
}

dependencies {
    // Compose BOM - manages all Compose library versions
    implementation(platform(libs.androidx.compose.bom))

    // Core Compose libraries (minimal set)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.lifecycle)

    // Compose tooling for previews (only in debug builds)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // AppCompat for language switching
    implementation(libs.androidx.appcompat)

    // Keep Material for theme compatibility
    implementation(libs.material)

    // Hilt - minimal DI setup
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room - database
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // DataStore - for preferences/settings
    implementation(libs.androidx.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
