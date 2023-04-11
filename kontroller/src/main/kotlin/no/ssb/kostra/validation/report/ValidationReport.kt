package no.ssb.kostra.validation.report

import io.micronaut.core.annotation.Introspected
import no.ssb.kostra.program.Arguments

@Introspected
class ValidationReport(
    private val arguments: Arguments,
    private val validationEntries: List<ValidationReportEntry>?
){
    private fun newline(): String = if (arguments.isRunAsExternalProcess) "" else "\n"

    val severity: Severity
        get() = validationEntries
            ?.map { it.severity }
            ?.maxByOrNull { it.ordinal } ?: Severity.OK

    fun generateReport() : String {
        return "" + newline()
    }
}

