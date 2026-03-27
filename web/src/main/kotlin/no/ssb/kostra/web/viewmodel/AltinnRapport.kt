package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.validation.report.Severity

@Serdeable
data class AltinnRapport(
    val respondent: AltinnRespondent,
    val antallKontroller: Int,
    val alvorlighetsgrad: Severity,
    val meldinger: List<AltinnRapportMelding>
)