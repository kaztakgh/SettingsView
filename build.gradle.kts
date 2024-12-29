// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.21" apply false
    id("com.android.library") version "8.6.0" apply false
//    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
    id("maven-publish")
    kotlin("jvm") version "1.9.21" apply false
}