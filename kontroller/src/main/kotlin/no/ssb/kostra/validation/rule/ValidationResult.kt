package no.ssb.kostra.validation.rule

import no.ssb.kostra.validation.report.ValidationReportEntry

data class ValidationResult(
    val reportEntries: List<ValidationReportEntry>,
    val numberOfControls: Int
)
