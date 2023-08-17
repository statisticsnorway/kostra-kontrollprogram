package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Plan02a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.PLAN_02A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.plan
        .filter {
            it.sluttDato != null
                    && it.startDato.isAfter(it.sluttDato)
        }.map { plan ->
            createValidationReportEntry(
                contextId = plan.id,
                messageText = "Plan (${plan.id}). Planens startdato (${plan.startDato}) er etter " +
                        "planens sluttdato (${plan.sluttDato})"
            )
        }.ifEmpty { null }
}
