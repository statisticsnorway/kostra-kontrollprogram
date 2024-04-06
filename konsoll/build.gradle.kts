plugins { kotlin("jvm") }
kotlin { jvmToolchain(17) }
repositories { mavenCentral() }
tasks.test { useJUnitPlatform() }

dependencies {
    implementation(project(":kostra-kontroller"))
    implementation(libs.picocli)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}