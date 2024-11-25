// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

buildscript {
    repositories {
        google() // 반드시 필요
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}