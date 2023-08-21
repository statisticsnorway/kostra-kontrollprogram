package no.ssb.kostra.validation.report

import java.time.LocalDateTime


data class ValidationResult(
    val reportEntries: List<ValidationReportEntry>,
    val numberOfControls: Int,
    val statsReportEntries: List<StatsReportEntry> = emptyList(),
    val endTime: LocalDateTime = LocalDateTime.now(),
) {
    val severity: Severity
        get() = if (numberOfControls == 0) Severity.ERROR else this.reportEntries.maxOf { it.severity }

    val count: Int
        get() = reportEntries.size

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