package no.ssb.kostra.validation.report

data class ValidationReportEntry(
    val severity: Severity = Severity.OK,
    val caseworker: String = "",
    val journalId: String = "",
    val individId: String = "",
    val contextId: String = "",
    val ruleName: String = "",
    val messageText: String = "",
    val lineNumbers: List<Int> = emptyList()
)