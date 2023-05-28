package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule

class Melding03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_03.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType): List<ValidationReportEntry>? {
        return super.validate(context)
    }
}