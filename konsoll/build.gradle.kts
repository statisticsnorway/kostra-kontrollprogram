application { mainClass = "no.ssb.kostra.program.KostraKontrollprogramCommand" }

plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":kostra-kontroller"))
    implementation(libs.picocli)
    runtimeOnly(libs.logback.classic)
}
