package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Individ10 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_10.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) =
        if (arguments.region.startsWith(OSLO_MUNICIPALITY_ID) && context.bydelsnavn == null) {
            createSingleReportEntryList(
                contextId = context.id,
                messageText = "Filen mangler bydelsnavn."
            )
        } else null
}