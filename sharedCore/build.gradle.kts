import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            baseName = "SharedCore"
            isStatic = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.sharedResources)
            api(projects.sharedTheme)

            api("org.jetbrains.compose.runtime:runtime:1.10.0")
            api("org.jetbrains.compose.foundation:foundation:1.10.0")
            api("org.jetbrains.compose.material3:material3:1.9.0")
            api("org.jetbrains.compose.ui:ui:1.10.0")
            api("org.jetbrains.compose.components:components-resources:1.10.0")
            api("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0")

            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtime.compose)

            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
            api(libs.koin.core)

            api(libs.kotlinx.serialization.json)
            api(libs.bundles.ktor)
            api(libs.bundles.coil)

            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            api(libs.datastore.preferences)
            api(libs.datastore)
        }

        androidMain.dependencies {
            api(projects.sharedResources)
            api(projects.sharedTheme)

            api(compose.preview)
            api(libs.androidx.activity.compose)

            api(libs.koin.android)
            api(libs.koin.androidx.compose)

            api(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            api(projects.sharedResources)
            api(projects.sharedTheme)

            api(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.wynndie.spwallet.sharedCore"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
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
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

