plugins {
    id("org.siouan.frontend-jdk21") version "10.0.0"
}

frontend {
    nodeVersion.set("20.15.0")
    assembleScript.set("run build")
    checkScript.set("run check")
}
