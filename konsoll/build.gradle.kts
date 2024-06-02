application { mainClass = "no.ssb.kostra.program.KostraKontrollprogramCommand" }

plugins {
    id("com.github.johnrengelman.shadow")
    application
}

dependencies {
    implementation(project(":kostra-kontroller"))
    implementation(libs.picocli)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}
