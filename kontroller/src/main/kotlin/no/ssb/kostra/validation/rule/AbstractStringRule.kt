package no.ssb.kostra.validation.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

abstract class AbstractStringRule(ruleName: String, severity: Severity) :
    AbstractRule<String>(ruleName, severity) {
    override fun validate(context: List<String>): List<ValidationReportEntry>? = null
}