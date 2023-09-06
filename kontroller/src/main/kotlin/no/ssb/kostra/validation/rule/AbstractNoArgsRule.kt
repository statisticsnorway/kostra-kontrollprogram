package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

abstract class AbstractNoArgsRule<in T : Any>(
    ruleName: String,
    severity: Severity
) : AbstractRule<T>(
    ruleName = ruleName,
    severity = severity
) {
    override fun validate(context: T, arguments: KotlinArguments): List<ValidationReportEntry>? = validate(context)

    protected abstract fun validate(context: T): List<ValidationReportEntry>?
}