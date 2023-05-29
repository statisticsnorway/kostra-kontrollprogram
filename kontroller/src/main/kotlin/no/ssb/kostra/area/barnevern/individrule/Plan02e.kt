package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Plan02e : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.PLAN_02E.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.plan
        .filter { it.startDato.isBefore(context.startDato) }
        .map { plan ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = plan.id,
                messageText = "Plan (${plan.id}). StartDato (${plan.startDato}) skal være lik eller " +
                        "etter individets startdato (${context.startDato})"
            )
        }.ifEmpty { null }
}