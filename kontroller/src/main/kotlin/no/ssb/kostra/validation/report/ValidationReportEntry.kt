package no.ssb.kostra.validation.report

import io.micronaut.core.annotation.Introspected

@Introspected
data class ValidationReportEntry(
    val severity: Severity = Severity.OK,
    val caseworker: String = "",
    val journalId: String = "",
    val ruleId: String = "",
    val ruleName: String = "",
    val messageText: String = "",
    val lineNumbers: List<Int> = emptyList()
)