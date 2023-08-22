package no.ssb.kostra.validation.report

import java.time.LocalDateTime


private fun List<ValidationReportEntry>.severity(): Severity =
    if (this.isEmpty()) Severity.OK
    else this.maxOf { it.severity }

data class ValidationResult(
    val reportEntries: List<ValidationReportEntry>,
    val numberOfControls: Int,
    val statsReportEntries: List<StatsReportEntry> = emptyList(),
    val endTime: LocalDateTime = LocalDateTime.now(),
) {
    val severity: Severity = if (numberOfControls == 0) Severity.ERROR else reportEntries.severity()
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