plugins {
    id("org.siouan.frontend-jdk21") version "9.1.0"
}

frontend {
    nodeVersion.set("20.18.2")
    assembleScript.set("run build")
    cleanScript.set("run clean")
    checkScript.set("run check")
}
