import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedResources)
            implementation(projects.sharedFeatures.home)
            implementation(projects.sharedFeatures.transfer)
            implementation(projects.sharedFeatures.edit)

            implementation(libs.jetbrains.compose.navigation)

            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        androidMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedResources)
            implementation(projects.sharedFeatures.home)
            implementation(projects.sharedFeatures.transfer)
            implementation(projects.sharedFeatures.edit)
        }

        iosMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedResources)
            implementation(projects.sharedFeatures.home)
            implementation(projects.sharedFeatures.transfer)
            implementation(projects.sharedFeatures.edit)
        }
    }
}

android {
    namespace = "com.wynndie.spwallet"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.wynndie.spwallet"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
        versionNameSuffix = "-beta1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false

            applicationIdSuffix = ".debug"
            versionNameSuffix = "_debug"

            manifestPlaceholders["usesCleartextTraffic"] = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            manifestPlaceholders["usesCleartextTraffic"] = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    android.applicationVariants.all {
        outputs.all {
            if (this is ApkVariantOutputImpl) {
                outputFileName = "SpWallet_v${versionName}_${name}.apk"
            }
        }
    }
}

dependencies {
    debugImplementation(libs.ui.tooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

