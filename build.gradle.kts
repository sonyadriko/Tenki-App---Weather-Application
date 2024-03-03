// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.0") // Sesuaikan versi Gradle sesuai kebutuhan
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22") // Sesuaikan versi Kotlin sesuai kebutuhan
        classpath("com.google.gms:google-services:4.3.15")
    }
}



plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false // Sesuaikan versi Kotlin sesuai kebutuhan
}
