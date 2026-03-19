package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.validation.report.Severity

@Serdeable
data class AltinnReport(
    val submitter: AltinnSubmitter,
    val controlsRunCount: Int,
    val severity: Severity,
    val entries: List<AltinnReportEntry>
)