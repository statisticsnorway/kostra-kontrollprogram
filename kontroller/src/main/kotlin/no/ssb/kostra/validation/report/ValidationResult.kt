package no.ssb.kostra.validation.report

import java.time.LocalDateTime

data class ValidationResult(
    val reportEntries: List<ValidationReportEntry>,
    val numberOfControls: Int,
    val statsReportEntries: List<StatsReportEntry> = emptyList(),
    val endTime: LocalDateTime = LocalDateTime.now(),
) {
    val severity: Severity = when {
        (numberOfControls == 0) -> Severity.ERROR
        reportEntries.isNotEmpty() -> reportEntries.maxBy { it.severity.ordinal }.severity
        else -> Severity.OK
    }

    val count: Int = reportEntries.size

    val uniqueReportEntries: List<ValidationReportEntry>
        get() = reportEntries
            .groupBy {
                listOf(it.caseworker, it.journalId, it.ruleName, it.messageText).joinToString("|")
            }.mapValues { (_, list) ->
                list.first().copy(
                    lineNumbers = list.flatMap { it.lineNumbers }.distinct().sorted()
                )
            }.values.toList()
}