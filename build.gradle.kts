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

val localLibs = extensions.getByType<VersionCatalogsExtension>().named("libs")

allprojects {
    if (name != "kostra-kontrollprogram-web-frontend") {
        repositories { mavenCentral() }

        apply(plugin = "org.jetbrains.kotlin.jvm")
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

        dependencies {
            implementation(localLibs.findLibrary("jackson.dataformat.yaml").get())
            implementation(localLibs.findLibrary("jackson.module.kotlin").get())
            implementation(localLibs.findLibrary("micronaut.serde.jackson").get())
            implementation(localLibs.findLibrary("micronaut.http.client").get())

            testImplementation(localLibs.findLibrary("micronaut.test.junit5").get())
            testImplementation(localLibs.findLibrary("kotest.assertions.core.jvm").get())
            testImplementation(localLibs.findLibrary("kotest.runner.junit5.jvm").get())
            testImplementation(localLibs.findLibrary("mockk.jvm").get())
            testImplementation(localLibs.findLibrary("assertj.core").get())
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
