plugins {
    kotlin("jvm") version "2.2.20"
}

group = "xyz.qweru"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":multirender-api"))
}

kotlin {
    jvmToolchain(21)
}