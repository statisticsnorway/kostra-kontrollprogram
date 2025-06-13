group = "no.ssb.kostra"

repositories { mavenCentral() }

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("org.sonarqube") version "6.2.0.5505"
    jacoco
}

dependencies {
    api(project(":kostra-kontroller"))

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${libs.versions.jackson.get()}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${libs.versions.jackson.get()}")
    implementation("io.micronaut.serde:micronaut-serde-jackson:${libs.versions.micronautSerde.get()}")
    implementation("io.micronaut:micronaut-http-client:${libs.versions.micronaut.get()}")
    implementation("io.micronaut.test:micronaut-test-junit5:4.5.0")

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
    testImplementation("org.assertj:assertj-core:3.26.3")
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


tasks.register<JavaExec>("generateMarkdownFromFileDescriptions") {
    group = "documentation"
    description = "Generates Markdown files from YAML in file_description_templates"

    val inputDir = file("kontroller/src/test/resources/file_description_templates")
    val outputDir = file("kravspesifikasjon")

    inputs.files(fileTree(inputDir) {
        include("file_description_*.yaml", "file_description_*.yml")
    })

    outputs.dir(outputDir)

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("GeneratorKt")
}

tasks.named("build") {
    dependsOn("generateMarkdownFromFileDescriptions")
}
