plugins {
    id("org.siouan.frontend-jdk21") version "9.0.0"
}

frontend {
    nodeVersion.set("22.14.0")
    assembleScript.set("run build")
    checkScript.set("run check")
}
