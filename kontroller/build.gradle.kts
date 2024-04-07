plugins {
    kotlin("jvm")
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
}
kotlin { jvmToolchain(17) }
repositories { mavenCentral() }
tasks.test { useJUnitPlatform() }

dependencies {
    api(project(":kostra-barnevern"))

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}