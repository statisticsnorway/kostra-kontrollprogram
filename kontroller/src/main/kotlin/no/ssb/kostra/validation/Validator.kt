package no.ssb.kostra.validation

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationResult

interface Validator {
    val arguments: KotlinArguments

    fun validate(): ValidationResult
}