package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule

class Undersokelse02c : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_02C.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType): List<ValidationReportEntry>? {
        return super.validate(context)
    }
}