plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}
dependencies {
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
    api(libs.logging.interceptor)
    api(libs.moshi.kotlin)
    api(libs.moshi)
    api(libs.moshi.converter)
    implementation(libs.retrofit.adapter)
}
