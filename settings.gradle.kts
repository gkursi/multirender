pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://jitpack.io")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "multirender"
include("multirender-api")
include("multirender-1-21-8")
include("multirender-nanovg")
include("multirender-twm")
include("multirender-gl-compat")
include("multirender-widget")
include("test", "test:test-1218")
