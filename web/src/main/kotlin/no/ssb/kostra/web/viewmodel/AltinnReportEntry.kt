package no.ssb.kostra.web.viewmodel

import no.ssb.kostra.validation.report.Severity

data class AltinnReportEntry(
    val severity: Severity,
    val ruleName: String,
    val messageText: String,
    val lineNumbers: List<Int>
)
