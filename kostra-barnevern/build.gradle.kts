plugins {
    alias(libs.plugins.spring.dependency.management)
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.2.4" apply false
    `maven-publish`
}

 if (!project.version.toString().contains("SNAPSHOT")) {
    apply(plugin = "com.google.cloud.artifactregistry.gradle-plugin")
 }

dependencyManagement {
    dependencies {
        dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${libs.versions.jackson.version.get()}")
    }
}

dependencies {
    // Transitive dependencies
    api(libs.jackson.module.kotlin)
    api(libs.jackson.dataformat.xml)
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Direct dependencies
    implementation(libs.jackson.jakarta.rs.xml.provider)
    implementation(libs.woodstox.core)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "no.ssb.kostra"
            artifactId = "kostra-barnevern"
            version = System.getenv("PROJECT_VERSION") ?: "LOCAL-SNAPSHOT"

            from(components["java"])
        }
    }
    repositories {
        maven("artifactregistry://europe-north1-maven.pkg.dev/${System.getenv("GAR_PROJECT_ID")}/kostra-maven")
    }
}
