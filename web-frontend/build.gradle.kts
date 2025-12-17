plugins {
    id("org.siouan.frontend-jdk21") version "10.0.0"
}

frontend {
    nodeDistributionProvided.set(false)
    nodeDistributionUrlRoot.set("https://nodejs.org/dist/")
    nodeVersion.set("24.12.0")

    installScript.set("run install")
    assembleScript.set("run build")
    checkScript.set("run check")

    nodeInstallDirectory.set(project.layout.projectDirectory.dir("node"))
    corepackVersion.set("latest")

    packageJsonDirectory.set(project.layout.projectDirectory)
    cacheDirectory.set(project.layout.projectDirectory.dir(".frontend-gradle-plugin"))
}
