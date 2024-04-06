version = project.findProperty("artifactRevision") ?: "LOCAL-SNAPSHOT"
// java -jar ./konsoll/build/libs/kostra-kontrollprogram-LOCAL-SNAPSHOT.jar --verbose

plugins { kotlin("jvm") }
kotlin { jvmToolchain(17) }
repositories { mavenCentral() }
tasks.test { useJUnitPlatform() }

dependencies {
    implementation(project(":kostra-kontroller"))
    implementation(libs.picocli)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(libs.mockk.jvm)
}

tasks.jar {
    manifest.attributes["Main-Class"] = "no.ssb.kostra.program.KostraKontrollprogramCommand"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}