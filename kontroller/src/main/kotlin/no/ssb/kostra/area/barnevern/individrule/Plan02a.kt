package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Plan02a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.PLAN_02A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.plan
        .filter {
            it.sluttDato != null
                    && it.startDato.isAfter(it.sluttDato)
        }.map { plan ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = plan.id,
                messageText = "Plan (${plan.id}). Planens startdato (${plan.startDato}) er etter " +
                        "planens sluttdato (${plan.sluttDato})"
            )
        }.ifEmpty { null }
}
