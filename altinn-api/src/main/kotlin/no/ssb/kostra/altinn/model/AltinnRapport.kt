package no.ssb.kostra.altinn.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AltinnRapport(
    val respondent: AltinnRespondent,
    val antallKontroller: Int,
    val alvorlighetsgrad: String,
    val meldinger: List<AltinnRapportMelding>,
    val versjon: String = ""
)