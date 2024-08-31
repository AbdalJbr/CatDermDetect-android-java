buildscript {
    val agp_version by extra("8.0.0")
    val agp_version1 by extra("7.4.1")

    repositories {
        mavenCentral()
        jcenter()
        google()
        maven("https://repo.spring.io/plugins-release")

    }
    dependencies{
        classpath ("com.android.tools.build:gradle:8.1.4")
        classpath ("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("com.google.gms:google-services:4.4.2")
        classpath ("org.codehaus.groovy.modules.http-builder:http-builder:0.7.2")
    }
}


apply(plugin = "com.jfrog.bintray")
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
}