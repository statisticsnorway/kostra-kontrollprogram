version = project.findProperty("artifactRevision") ?: "LOCAL-SNAPSHOT"
// java -jar ./web/build/libs/kostra-kontrollprogram-web-LOCAL-SNAPSHOT.jar --verbose

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("io.micronaut.application") version "4.3.5"
}

kotlin { jvmToolchain(17) }
repositories { mavenCentral() }
application { mainClass.set("no.ssb.kostra.web.ApplicationKt") }

dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.validation:micronaut-validation-processor")
    ksp("io.micronaut.openapi:micronaut-openapi")

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)

    implementation(project(":kostra-kontroller"))

    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.views:micronaut-views-thymeleaf")

    implementation("io.projectreactor:reactor-core")
    implementation(libs.swagger.annotations)
    implementation(libs.logback.classic)

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.projectreactor:reactor-test")
}

micronaut {
    version = libs.versions.micronautPlatform
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("no.ssb.kostra.*")
    }
}

tasks.jar {
    manifest.attributes["Main-Class"] = application.mainClass
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}