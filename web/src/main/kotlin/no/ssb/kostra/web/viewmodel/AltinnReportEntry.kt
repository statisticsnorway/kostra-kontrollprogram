package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.validation.report.Severity

@Serdeable
data class AltinnReportEntry(
    val severity: Severity,
    val ruleName: String,
    val messageText: String,
    val lineNumbers: List<Int>
)
