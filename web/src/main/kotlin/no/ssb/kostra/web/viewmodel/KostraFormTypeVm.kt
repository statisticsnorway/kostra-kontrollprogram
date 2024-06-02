package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class KostraFormTypeVm(
    val id: String,
    val tittel: String,
    val kvartal: String?,
    val labelOrgnr: String?,
    val labelOrgnrVirksomhetene: String?
)
