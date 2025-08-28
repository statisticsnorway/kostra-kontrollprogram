plugins {
    alias(libs.plugins.spring.dependency.management)
    `maven-publish`
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencyManagement {
    dependencies {
        dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${libs.versions.jackson.version.get()}")
    }
}

dependencies {
    // Transitive dependencies
    api(libs.jackson.module.kotlin)
    api(libs.jackson.dataformat.xml)
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Direct dependencies
    implementation(libs.jackson.jakarta.rs.xml.provider)
    implementation(libs.woodstox.core)
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            groupId = "no.ssb.kostra"
            artifactId = "kostra-barnevern"
            version = project.findProperty("version") as String? ?: "LOCAL-SNAPSHOT"
            from(components["java"])
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf {
        project.hasProperty("enablePublishing")
    }
}
