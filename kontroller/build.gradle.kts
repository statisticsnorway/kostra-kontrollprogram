plugins {
    alias(libs.plugins.gradle.git.properties)
}

dependencies {
    api(project(":kostra-barnevern"))
}

gitProperties {
    dotGitDirectory.set(file("${rootProject.projectDir}/.git"))
}
