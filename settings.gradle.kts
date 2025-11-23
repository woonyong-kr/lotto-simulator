rootProject.name = "lotto-simulator"

include("lotto-central-server")
include("lotto-pos-manager")
include("lotto-pos-terminal")
include("lotto-bot-client")
include("lotto-dashboard")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, ".gradle/build-cache")
    }
}

gradle.startParameter.apply {
    isParallelProjectExecutionEnabled = true
    isBuildCacheEnabled = true
}
