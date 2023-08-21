package no.ssb.kostra.validation.report

import no.ssb.kostra.program.KotlinArguments

data class ValidationReportArguments(
    val kotlinArguments: KotlinArguments,
    val validationResult: ValidationResult,
)