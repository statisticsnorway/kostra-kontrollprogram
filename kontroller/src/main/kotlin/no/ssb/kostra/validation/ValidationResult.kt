package no.ssb.kostra.validation


import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.report.ValidationReportEntry

data class ValidationResult(
    val reportEntries: List<ValidationReportEntry>,
    val numberOfControls: Int,
    val statsEntries: List<StatsReportEntry> = emptyList()
)