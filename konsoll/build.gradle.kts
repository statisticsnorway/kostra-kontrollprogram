version = project.findProperty("artifactRevision") ?: "LOCAL-SNAPSHOT"
// ./gradlew -p konsoll clean shadowJar
// java -jar ./konsoll/build/libs/kostra-kontrollprogram-LOCAL-SNAPSHOT-all.jar --verbose

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

kotlin { jvmToolchain(17) }
repositories { mavenCentral() }
tasks.test { useJUnitPlatform() }

dependencies {
    implementation(project(":kostra-kontroller"))
    implementation(libs.picocli)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}