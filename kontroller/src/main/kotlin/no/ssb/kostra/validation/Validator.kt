package no.ssb.kostra.validation

import no.ssb.kostra.program.KotlinArguments

abstract class Validator(
    open val arguments: KotlinArguments
)  {
    abstract fun validate(): ValidationResult
}