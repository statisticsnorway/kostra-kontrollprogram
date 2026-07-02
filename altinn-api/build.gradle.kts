application { mainClass = "no.ssb.kostra.altinn.ApplicationKt" }

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
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.views:micronaut-views-thymeleaf")
    implementation("io.projectreactor:reactor-core")
    implementation(libs.logback.classic)
    implementation(libs.micronaut.security.jwt)
    implementation(libs.swagger.annotations)

    implementation(project(":kostra-kontroller"))

    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("io.micronaut:micronaut-jackson-databind")

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.micronaut.security:micronaut-security")
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
    jvmArgs?.add("-Dkotest.framework.config.fqn=no.ssb.kostra.altinn.ProjectConfig")
}