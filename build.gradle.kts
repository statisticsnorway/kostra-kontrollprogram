import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

group = "no.ssb.kostra"

repositories { mavenCentral() }

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.sonarqube)
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
        repositories { mavenCentral() }

        apply(plugin = "kotlin")
        apply(plugin = "jacoco")

        configure<KotlinJvmProjectExtension> {
            jvmToolchain(21)
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            jvmArgs("-Xshare:off", "-XX:+EnableDynamicAgentLoading")
        }

        tasks.withType<JacocoReport> {
            dependsOn(tasks.withType<Test>())
            reports {
                xml.required.set(true)
            }
        }
    }
}
