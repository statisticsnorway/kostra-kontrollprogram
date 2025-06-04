plugins {
    id("com.gorylenko.gradle-git-properties") version "2.5.0"
}

dependencies {
    api(project(":kostra-barnevern"))

    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.micronaut.http.client)
    implementation(libs.micronaut.serde.jackson)

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.micronaut.test.junit5)
    testImplementation(libs.mockk.jvm)
}

gitProperties {
    dotGitDirectory.set(file("${rootProject.projectDir}/.git"))
}
