plugins {
    id("io.spring.dependency-management") version "1.1.6"
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.2.2"
    `maven-publish`
}

dependencyManagement {
    dependencies {
        dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${libs.versions.jackson.get()}")
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

    // Test dependencies
    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "no.ssb.kostra"
            artifactId = "kostra-barnevern"
            version = rootProject.version.toString()

            from(components["java"])
        }
    }
    repositories {
        maven("artifactregistry://europe-north1-maven.pkg.dev/artifact-registry-5n/kostra-maven")
    }
}
