package no.ssb.kostra.validation.report

import no.ssb.kostra.program.KotlinArguments

class ValidationReport(
    private val arguments: KotlinArguments,
    private val validationEntries: List<ValidationReportEntry>?
){
    private fun newline(): String = if (arguments.isRunAsExternalProcess) "" else "\n"

    val severity: Severity
        get() = validationEntries
            ?.map { it.severity }
            ?.maxByOrNull { it.ordinal } ?: Severity.OK

    /** TODO: Incomplete */
    fun generateReport() : String = newline()
}

