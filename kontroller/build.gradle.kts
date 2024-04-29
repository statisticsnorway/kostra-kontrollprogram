plugins {
    id("com.gorylenko.gradle-git-properties") version "2.4.2"
}

dependencies {
    api(project(":kostra-barnevern"))

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}