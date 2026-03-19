package no.ssb.kostra.web.viewmodel

import no.ssb.kostra.validation.report.Severity

data class AltinnReport(
    val submitter: AltinnSubmitter,
    val controlsRunCount: Int,
    val severity: Severity,
    val entries: List<AltinnReportEntry>
)