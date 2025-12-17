import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.siouan.frontendgradleplugin.infrastructure.gradle.RunNodeTaskType
import org.siouan.frontendgradleplugin.infrastructure.gradle.RunNpmTaskType

plugins {
    id("org.siouan.frontend-jdk21") version "9.1.0"
}

//val preinstalledNodeDistributionDirectory = file(<path-to-nodejs-install-directory>);
val preinstalledNodeDistributionDirectory = null as File?;

frontend {
    nodeDistributionProvided.set(true)
    nodeInstallDirectory.set(preinstalledNodeDistributionDirectory)
    nodeVersion.set("24.12.0")
    assembleScript.set("run build")
    checkScript.set("run check")
}

tasks.register<RunNodeTaskType>("nodeVersion") {
    description = "Triggers install Node for this project."
    group = "node.js"
    dependsOn("installNode")
    args.set("-v")
}

tasks.register<RunNpmTaskType>("npmVersion") {
    description = "Triggers install npm for this project."
    group = "npm"
    dependsOn("installPackageManager")
    args.set("-v")
}
