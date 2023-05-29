package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule

class Avgiver01 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_01.title,
    Severity.FATAL) {

    override fun validate(context: KostraAvgiverType): List<ValidationReportEntry>? {
        return super.validate(context)
    }
}