package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AltinnRapportMelding(
    val alvorlighetsgrad: String,
    val kontrollNavn: String,
    val meldingTekst: String,
    val linjenumre: List<Int>
)
