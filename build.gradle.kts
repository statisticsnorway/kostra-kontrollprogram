group = "no.ssb.kostra"

repositories { mavenCentral() }

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("org.sonarqube") version "6.0.1.5171"
    jacoco
}

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
    if (name != "kostra-kontrollprogram-web-frontend") {
        apply(plugin = "kotlin")
        apply(plugin = "jacoco")

        kotlin { jvmToolchain(21) }
        repositories { mavenCentral() }

        tasks.test {
            useJUnitPlatform()
            jvmArgs("-Xshare:off", "-XX:+EnableDynamicAgentLoading")
        }

        tasks.jacocoTestReport {
            dependsOn(tasks.test)
            reports { xml.required = true }
        }
    }
}