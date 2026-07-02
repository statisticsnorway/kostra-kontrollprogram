package no.ssb.kostra.altinn.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AltinnRapportMelding(
    val alvorlighetsgrad: String,
    val kontrollNavn: String,
    val meldingTekst: String,
    val linjenumre: List<Int>
)
