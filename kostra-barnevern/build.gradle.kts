plugins {
    kotlin("jvm")
    id("io.spring.dependency-management") version "1.1.4"
    `maven-publish`
}
kotlin { jvmToolchain(17) }
repositories { mavenCentral() }
tasks.test { useJUnitPlatform() }

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
    repositories {
        maven("artifactregistry://europe-north1-maven.pkg.dev/artifact-registry-14da/maven-releases")
    }
}
