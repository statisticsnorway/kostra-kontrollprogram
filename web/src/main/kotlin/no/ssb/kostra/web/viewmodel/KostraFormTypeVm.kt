package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable
data class KostraFormTypeVm(
    val id: String,
    val tittel: String,
    val kvartal: String?,
    val labelOrgnr: String?,
)
