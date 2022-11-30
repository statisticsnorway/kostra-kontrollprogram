package no.ssb.kostra.web.status

import io.micronaut.core.annotation.Introspected

@Introspected
data class GitProperties(
    val tags: String
)
