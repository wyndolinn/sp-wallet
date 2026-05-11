import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        namespace = "com.wynndie.spwallet"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            api(projects.sharedCore)
        }

        commonMain.dependencies {
            implementation(projects.sharedCore)
            implementation(projects.sharedFeatures.home)
            implementation(projects.sharedFeatures.transfer)
            implementation(projects.sharedFeatures.edit)

            implementation(libs.jetbrains.compose.navigation)
        }

        iosMain.dependencies {
            api(projects.sharedCore)
        }
    }
}

