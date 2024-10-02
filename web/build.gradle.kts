application { mainClass = "no.ssb.kostra.web.ApplicationKt" }

plugins {
    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
    id("io.micronaut.application") version "4.4.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.validation:micronaut-validation-processor")
    ksp("io.micronaut.openapi:micronaut-openapi")

    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.views:micronaut-views-thymeleaf")
    implementation("io.projectreactor:reactor-core")
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.logback.classic)
    implementation(libs.swagger.annotations)

    implementation(project(":kostra-kontroller"))

    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("io.micronaut:micronaut-jackson-databind")

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

tasks.register<Copy>("processFrontendResources") {
    val backendTargetDir = project.layout.buildDirectory.dir("resources/main/static")
    val frontendBuildDir =
        project(":kostra-kontrollprogram-web-frontend").layout.projectDirectory.dir("dist")

    group = "Frontend"
    description = "Process frontend resources"
    dependsOn(":kostra-kontrollprogram-web-frontend:assembleFrontend")

    from(frontendBuildDir)
    into(backendTargetDir)
}

tasks.named("processResources") {
    dependsOn("processFrontendResources")
}