package no.ssb.kostra.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class KostraFormTypeVm(
    val id: String,
    val tittel: String,
    val labelOrgnr: String?,
    val labelOrgnrVirksomhetene: String?
)
