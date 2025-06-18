plugins {
    alias(libs.plugins.gradle.git.properties)
}

dependencies {
    api(project(":kostra-barnevern"))

    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.module.kotlin)
    compileOnly(libs.micronaut.serde.jackson)
}

gitProperties {
    dotGitDirectory.set(file("${rootProject.projectDir}/.git"))
}
