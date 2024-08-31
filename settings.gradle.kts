pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
        google()
        maven("https://repo.spring.io/plugins-release")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.google.com")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://jcenter.bintray.com/")
        maven(url = "https://repo.spring.io/plugins-release")
        mavenCentral()

    }
}

rootProject.name = "CatDermDetect"
include(":app")
