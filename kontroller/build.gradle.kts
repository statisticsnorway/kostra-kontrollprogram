plugins {
    id("com.gorylenko.gradle-git-properties") version "2.5.0"
}

dependencies {
    api(project(":kostra-barnevern"))

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${libs.versions.jackson.get()}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${libs.versions.jackson.get()}")

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}