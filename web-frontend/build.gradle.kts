plugins {
    alias(libs.plugins.siouan.frontend.jdk21)
}

frontend {
    nodeVersion.set("24.12.0")
    assembleScript.set("run build")
    checkScript.set("run check")
}
