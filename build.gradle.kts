group = "no.ssb.kostra"

plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}
repositories { mavenCentral() }