rootProject.name = "kostra-kontrollprogram-parent"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include("kostra-kontrollprogram")
project(":kostra-kontrollprogram").projectDir = file("konsoll")

include("kostra-kontroller")
project(":kostra-kontroller").projectDir = file("kontroller")

include("kostra-barnevern")
project(":kostra-barnevern").projectDir = file("kostra-barnevern")

include("kostra-kontrollprogram-web")
project(":kostra-kontrollprogram-web").projectDir = file("web")

include("kostra-kontrollprogram-web-frontend")
project(":kostra-kontrollprogram-web-frontend").projectDir = file("web-frontend")
