package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

abstract class AbstractRecordRule(ruleName: String, severity: Severity) :
    AbstractRule<KostraRecord>(ruleName, severity) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = null
}