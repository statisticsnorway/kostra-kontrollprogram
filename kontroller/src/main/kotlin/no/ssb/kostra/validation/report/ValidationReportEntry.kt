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
) : Comparable<ValidationReportEntry> {
    override fun compareTo(other: ValidationReportEntry): Int {
        return compareValuesBy(this, other, { it.caseworker }, { it.journalId }, {it.ruleName})
    }
}