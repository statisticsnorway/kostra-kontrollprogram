pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // Artifact Registry repo — replace with your details
        maven {
            url = uri("artifactregistry://europe-north1-maven.pkg.dev/${System.getenv("GAR_PROJECT_ID")}/kostra-maven")
        }
    }
}
