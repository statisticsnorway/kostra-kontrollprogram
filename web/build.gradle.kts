application { mainClass = "no.ssb.kostra.web.ApplicationKt" }

plugins {
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.micronaut.application)
    alias(libs.plugins.shadow)
}

dependencies {
    // https://micronaut-projects.github.io/micronaut-serialization/latest/guide/
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.validation:micronaut-validation-processor")
    ksp("io.micronaut.openapi:micronaut-openapi")

    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.views:micronaut-views-thymeleaf")
    implementation("io.projectreactor:reactor-core")
    implementation(libs.logback.classic)
    implementation(libs.swagger.annotations)

    implementation(project(":kostra-kontroller"))

    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("io.micronaut:micronaut-jackson-databind")

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(libs.kotest.assertions.json.jvm)
}

micronaut {
    version = libs.versions.micronaut.platform.version
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("no.ssb.kostra.*")
    }
}

tasks.test {
    @Suppress("UNNECESSARY_SAFE_CALL")
    jvmArgs?.add("-Dkotest.framework.config.fqn=no.ssb.kostra.web.ProjectConfig")
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