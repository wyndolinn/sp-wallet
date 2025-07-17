import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.jetbrains.kotlin.serialization)
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

        androidMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedResources)
            implementation(projects.sharedFeature.home)
            implementation(projects.sharedFeature.transfer)
            implementation(projects.sharedFeature.edit)
        }

        commonMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedResources)
            implementation(projects.sharedFeature.home)
            implementation(projects.sharedFeature.transfer)
            implementation(projects.sharedFeature.edit)

            implementation(libs.jetbrains.compose.navigation)

            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        iosMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedResources)
            implementation(projects.sharedFeature.home)
            implementation(projects.sharedFeature.transfer)
            implementation(projects.sharedFeature.edit)
        }

        dependencies {
            ksp(libs.androidx.room.compiler)
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
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

