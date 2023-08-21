package no.ssb.kostra.validation

import no.ssb.kostra.program.KotlinArguments

interface Validator{
    val arguments: KotlinArguments

    fun validate(): ValidationResult
}