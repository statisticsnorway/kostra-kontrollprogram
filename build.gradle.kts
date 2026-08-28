import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

group = "no.ssb.kostra"

repositories { mavenCentral() }

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sonarqube)
    jacoco
}

dependencies {
    implementation(project(":kostra-kontroller"))
    implementation(libs.jackson.dataformat.yaml)

    compileOnly(libs.micronaut.serde.jackson)

    testImplementation(libs.assertj.core)
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
            **/ApiController.kt
            **/node_modules/**/*,
            **/KostraRecordExtensionsGenerics.kt,
            **/KostraKontrollprogramCommand.kt,
            **/MappingToConsoleAppExtensions.kt,
            **/kostra/barnevern/**/*,
            **/gradletask/**/*
            """.trimIndent(),
        )
    }
}

val localLibs = extensions.getByType<VersionCatalogsExtension>().named("libs")

allprojects {
    if (name != "kostra-kontrollprogram-web-frontend") {
        repositories { mavenCentral() }

        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "jacoco")

        configure<KotlinJvmProjectExtension> {
            jvmToolchain(25)
            compilerOptions {
                freeCompilerArgs
                    .addAll(
                        "-Xjsr305=strict"
                    )
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            jvmArgs(
                "-Xshare:off",
                "-XX:+EnableDynamicAgentLoading",
                "-Dkotest.framework.classpath.scanning.autoscan.disable=true"
            )
        }

        tasks.withType<JacocoReport> {
            dependsOn(tasks.withType<Test>())
            reports {
                xml.required.set(true)
            }
        }

        dependencies {
            implementation(enforcedPlatform(localLibs.findLibrary("netty.bom").get()))
            implementation(enforcedPlatform(localLibs.findLibrary("jackson.bom").get()))
            testImplementation(localLibs.findLibrary("kotest.runner.junit5.jvm").get())
            testImplementation(localLibs.findLibrary("kotest.assertions.core.jvm").get())
            testImplementation(localLibs.findLibrary("mockk.jvm").get())
        }
    }
}
tasks.register<gradletask.GenerateResourcesAndDocsTask>("generateMarkdownFromFileDescriptions") {
    group = "documentation"
    description = "Generates Markdown files from YAML in file_description_templates"

    inputBaseDir.set(layout.projectDirectory.dir("buildSrc/src/main/resources"))
    specsOutputDir.set(layout.projectDirectory.dir("kravspesifikasjon"))
    resourcesOutputDir.set(layout.projectDirectory.dir("kontroller/src/main/resources"))
}

project(":kostra-kontroller") {
    tasks.named<ProcessResources>("processResources") {
        dependsOn(rootProject.tasks.named("generateMarkdownFromFileDescriptions"))
    }
}
