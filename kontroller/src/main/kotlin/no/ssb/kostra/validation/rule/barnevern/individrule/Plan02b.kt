package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Plan02b : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.PLAN_02B.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.plan
        .filter {
            it.sluttDato?.let { sluttDato -> sluttDato.year != arguments.aargang.toInt() } ?: false
        }.map { plan ->
            createValidationReportEntry(
                contextId = plan.id,
                messageText = "Plan (${plan.id}). Planens sluttdato (${plan.sluttDato}) er ikke " +
                        "i rapporteringsåret (${arguments.aargang})"
            )
        }.ifEmpty { null }
}
