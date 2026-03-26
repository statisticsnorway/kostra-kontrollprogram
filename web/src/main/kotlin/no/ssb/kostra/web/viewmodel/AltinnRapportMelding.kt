package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.validation.report.Severity

@Serdeable
data class AltinnRapportMelding(
    val alvorlighetsgrad: Severity,
    val kontrollNavn: String,
    val meldingTekst: String,
    val linjenumre: List<Int>
)
