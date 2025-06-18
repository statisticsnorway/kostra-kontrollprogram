plugins {
    alias(libs.plugins.gradle.git.properties)
}

dependencies {
    api(project(":kostra-barnevern"))

    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.module.kotlin)

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}

gitProperties {
    dotGitDirectory.set(file("${rootProject.projectDir}/.git"))
}
