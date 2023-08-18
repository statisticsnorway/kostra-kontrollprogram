package no.ssb.kostra.web

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "\${api.title}",
        version = "\${api.version}",
        description = "\${api.description}"
    )
)
object Api

fun main(args: Array<String>) {
    run(*args)
}

