package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ09 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_09.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) =
        if (arguments.region.startsWith("0301") && context.bydelsnummer == null) {
            createSingleReportEntryList(
                journalId = context.journalnummer,
                contextId = context.id,
                messageText = "Filen mangler bydelsnummer."
            )
        } else null
}