import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.android.build.gradle.tasks.PackageApplication
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("debug") {

        }

        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    android.applicationVariants.all {
        outputs.all {
            val date = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(Date())
            if (this is ApkVariantOutputImpl) {
                outputFileName = "SpWallet_v${versionName}_${date}_${name}.apk"
            }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

