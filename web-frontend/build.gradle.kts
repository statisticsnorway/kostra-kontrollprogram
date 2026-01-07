plugins {
    id("com.github.node-gradle.node") version "5.0.0"
}

node {
    version.set("24.12.0")         // Node version to use
    npmVersion.set("11.6.2")         // Optional npm version
    download.set(true)              // auto-download Node

    workDir.set(layout.buildDirectory.dir("nodejs"))
    npmWorkDir.set(layout.buildDirectory.dir("npm"))

    nodeProjectDir.set(projectDir)
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("assembleFrontend") {
    description = "Generates the frontend for this project."
    group = JavaBasePlugin.BUILD_TASK_NAME
    dependsOn(tasks.npmInstall)
    args.set(listOf("run", "build"))
}
