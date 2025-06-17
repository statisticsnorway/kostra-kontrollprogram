package no.ssb.kostra.web.viewmodel

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.validation.report.Severity

@Introspected
@Serdeable
@JsonInclude(JsonInclude.Include.ALWAYS)
data class FileReportEntryVm(
    val severity: Severity,
    val caseworker: String,
    val journalId: String,
    val individId: String,
    val contextId: String,
    val ruleName: String,
    val messageText: String,
    val lineNumbers: List<Int>
)
