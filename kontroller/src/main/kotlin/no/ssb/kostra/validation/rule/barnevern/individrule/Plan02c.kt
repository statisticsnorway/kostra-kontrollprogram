package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Plan02c : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.PLAN_02C.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context
        .takeIf { it.sluttDato != null }
        ?.let { innerContext ->
            innerContext.plan.filter {
                it.sluttDato != null
                        && it.sluttDato.isAfter(context.sluttDato)
            }.map { plan ->
                createValidationReportEntry(
                    journalId = innerContext.journalnummer,
                    contextId = plan.id,
                    messageText = "Plan (${plan.id}). Planens sluttdato (${plan.sluttDato}) er etter " +
                            "individets sluttdato (${context.sluttDato})"
                )
            }.ifEmpty { null }
        }
}