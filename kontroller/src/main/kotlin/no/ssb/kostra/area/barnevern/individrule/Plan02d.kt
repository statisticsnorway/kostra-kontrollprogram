package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.SharedValidationConstants
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import java.time.LocalDate

class Plan02d : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.PLAN_02D.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context
        .takeIf { it.avslutta3112 == SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE }
        ?.let { innerContext ->
            innerContext.plan.filter {
                it.sluttDato == null
                        || innerContext.sluttDato == null
                        || it.sluttDato.isAfter(LocalDate.of(arguments.aargang.toInt(), 12, 31))
            }.map { plan ->
                createReportEntry(
                    journalId = innerContext.journalnummer,
                    contextId = plan.id,
                    messageText = "Plan (${plan.id}). Individet er avsluttet hos barnevernet og dets planer skal " +
                            "dermed være avsluttet. Sluttdato er ${plan.sluttDato ?: "uoppgitt"}"
                )
            }.ifEmpty { null }
        }
}