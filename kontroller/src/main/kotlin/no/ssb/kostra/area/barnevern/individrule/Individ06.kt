package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ06 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_06.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.takeIf {
        it.melding.none()
                && it.plan.none()
                && it.tiltak.none()
    }?.let { individ ->
        createSingleReportEntryList(
            journalId = individ.journalnummer,
            contextId = context.id,
            messageText = "Individet har ingen meldinger, planer eller tiltak i løpet av året"
        )
    }
}