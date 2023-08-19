package no.ssb.kostra.common.viewmodel

import io.micronaut.core.annotation.Introspected
import jakarta.validation.constraints.Pattern

@Introspected
data class CompanyIdVm(

    @field:Pattern(regexp = "\\d{9}", message = "Må starte med 8 eller 9 etterfulgt av 8 siffer")
    val orgnr: String = ""
)
