package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Individ06 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_06.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.takeIf {
        it.melding.none()
                && it.plan.none()
                && it.tiltak.none()
    }?.let {
        createSingleReportEntryList(
            contextId = context.id,
            messageText = "Individet har ingen meldinger, planer eller tiltak i løpet av året"
        )
    }
}