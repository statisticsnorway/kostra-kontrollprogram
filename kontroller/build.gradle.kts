plugins {
    alias(libs.plugins.gradle.git.properties)
}

dependencies {
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)
    compileOnly(libs.micronaut.serde.jackson)
}

gitProperties {
    dotGitDirectory.set(file("${rootProject.projectDir}/.git"))
}
