// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.safeargs) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.objectbox.io")
        gradlePluginPortal()  // Добавляем репозиторий для плагинов
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.2")  // Зависимость для плагина Android
        classpath("io.objectbox:objectbox-gradle-plugin:3.7.0")  // Плагин ObjectBox
        classpath("com.squareup:javapoet:1.13.0")
    }
}

