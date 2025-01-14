group = "no.ssb.kostra"

repositories { mavenCentral() }

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("org.sonarqube") version "6.0.1.5171"
    id("com.google.cloud.tools.jib") version "3.4.4"
    jacoco
}

sonarqube {
    properties {
        property("sonar.organization", "statisticsnorway")
        property("sonar.projectKey", "statisticsnorway_kostra-kontrollprogram-parent")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.exclusions",
            """
            **/Application.kt,
            **/node_modules/**/*,
            **/KostraRecordExtensionsGenerics.kt,
            **/KostraKontrollprogramCommand.kt,
            **/MappingToConsoleAppExtensions.kt,
            **/kostra/barnevern/**/*
            """.trimIndent(),
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

jib {
    from.image = "bellsoft/liberica-openjdk-alpine:21"
    container.creationTime = "USE_CURRENT_TIMESTAMP"
}
