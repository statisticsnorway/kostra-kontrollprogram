group = "no.ssb.kostra"

plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("org.sonarqube") version "5.0.0.4638"
    jacoco
}
repositories { mavenCentral() }

sonarqube {
    properties {
        property("sonar.organization", "statisticsnorway")
        property("sonar.projectKey", "statisticsnorway_kostra-kontrollprogram-parent")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.exclusions", """
                **/Application.kt,
                **/node_modules/**/*,
                **/KostraRecordExtensionsGenerics.kt,
                **/KostraKontrollprogramCommand.kt,
                **/MappingToConsoleAppExtensions.kt,
                **/kostra/barnevern/**/*
                """.trimIndent()
        )
    }
}

subprojects {
    apply(plugin = "jacoco")

    tasks.withType<JacocoReport> {
        dependsOn(tasks.withType<Test>())
        reports { xml.required = true }
    }
}