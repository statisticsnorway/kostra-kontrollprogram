plugins {
    id("org.siouan.frontend-jdk21") version "8.1.0"
}

frontend {
    nodeVersion.set("20.15.0")
    assembleScript.set("run build")
    cleanScript.set("run clean")
    checkScript.set("run check")
}