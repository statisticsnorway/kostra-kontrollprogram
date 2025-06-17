import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

group = "no.ssb.kostra"

repositories { mavenCentral() }

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.sonarqube)
    jacoco
}

dependencies {
////    api(project(":kostra-kontroller"))
//
//    implementation(libs.jackson.dataformat.yaml)
//    implementation(libs.jackson.module.kotlin)
////    implementation("io.micronaut.serde:micronaut-serde-jackson:${libs.versions.micronautSerde.get()}")
////    implementation("io.micronaut:micronaut-http-client:${libs.versions.micronaut.get()}")
////    implementation("io.micronaut.test:micronaut-test-junit5:${libs.versions.micronautTestJunit5.get()}")
//
//    testImplementation(libs.kotest.assertions.core.jvm)
//    testImplementation(libs.kotest.runner.junit5.jvm)
//    testImplementation(libs.mockk.jvm)
//    testImplementation("org.assertj:assertj-core:${libs.versions.assertj.get()}")
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
            **/kostra/barnevern/**/*,
            **/gradletask/**/*
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

//tasks.test {
//    useJUnitPlatform() // IMPORTANT for Kotest 5+
//}
//
//tasks.register<JavaExec>("generateMarkdownFromFileDescriptions") {
//    group = "documentation"
//    description = "Generates Markdown files from YAML in file_description_templates"
//
//    val inputDir = file("kontroller/src/test/resources/file_description_templates")
//    val outputDir = file("kravspesifikasjon")
//
//    inputs.files(fileTree(inputDir) {
//        include("file_description_*.yaml", "file_description_*.yml")
//    })
//
//    outputs.dir(outputDir)
//
//    classpath = sourceSets["main"].runtimeClasspath
//    mainClass.set("gradletask.ApplicationKt")
//}
//
//tasks.named("build") {
//    dependsOn("generateMarkdownFromFileDescriptions")
//}
