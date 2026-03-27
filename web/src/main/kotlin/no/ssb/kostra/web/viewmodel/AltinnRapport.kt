package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AltinnRapport(
    val respondent: AltinnRespondent,
    val antallKontroller: Int,
    val alvorlighetsgrad: String,
    val meldinger: List<AltinnRapportMelding>
)