package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class KostraFormType(
    val id: String,
    val tittel: String,
    val labelOrgnr: String? = null,
    val labelOrgnrVirksomhetene: String? = null
)
