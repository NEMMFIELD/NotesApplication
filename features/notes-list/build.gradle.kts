import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree.Companion.test

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.example.notes.list"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation (libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.robolectric)
    testImplementation(kotlin("test"))
    testImplementation (libs.turbine)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)


    implementation(libs.fragment.kotlin)
    implementation(libs.recyclerview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // Dagger Hilt and KSP dependencies
    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler) // Используем KSP для Hilt
    implementation ("io.objectbox:objectbox-kotlin:3.7.0")
    implementation("io.objectbox:objectbox-android:3.7.0")  // Добавляем зависимость для Android
    implementation("io.objectbox:objectbox-kotlin:3.7.0")  // Зависимость для Kotlin
    implementation("com.squareup:javapoet:1.13.0")

    implementation(project(":core:notes-api"))
    implementation(project(":core:database"))
    implementation(project(":core:state"))
    implementation(project(":core:utils"))
    implementation(project(":features:notes-edit"))
}

