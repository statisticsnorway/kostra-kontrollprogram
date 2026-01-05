plugins {
    id("org.siouan.frontend-jdk21") version "9.0.0"
}

frontend {
    nodeVersion.set("20.19.6")
    nodeDistributionUrlRoot.set("https://nodejs.org/dist/")
    assembleScript.set("run build")
    checkScript.set("run check")
}
