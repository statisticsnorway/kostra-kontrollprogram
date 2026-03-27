package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import no.ssb.kostra.web.validation.ValidFormType

@Serdeable
data class AltinnRespondent(
    @field:Min(value = 2026, message = "Year kan ikke være mindre enn {value}")
    val aar: Int = 0,

    @field:Pattern(regexp = "^[1-4 ]$", message = "Kvartal er én av: 1, 2, 3, 4 eller et mellomrom for årlig rapportering. Default er mellomrom for årlig rapportering.")
    val kvartal: String = " ",

    @field:ValidFormType
    val skjema: String = "",

    @field:Pattern(regexp = "\\d{6}", message = "Region må bestå av 6 siffer uten mellomrom")
    val region: String = "",

    @field:Pattern(regexp = "[8|9]\\d{8}", message = "Må starte med 8 eller 9 etterfulgt av 8 siffer")
    val orgnr: String = " ".repeat(9)
)
