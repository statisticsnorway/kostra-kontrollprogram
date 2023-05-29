package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule

class Avgiver04 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_04.title,
    Severity.FATAL) {

    override fun validate(context: KostraAvgiverType, arguments: Arguments): List<ValidationReportEntry>? {
        return super.validate(context, arguments)
    }
}