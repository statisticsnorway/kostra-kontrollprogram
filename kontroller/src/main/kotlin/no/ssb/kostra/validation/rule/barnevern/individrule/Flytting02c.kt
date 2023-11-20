package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Flytting02c : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.FLYTTING_02C.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context
        .takeIf { it.sluttDato != null }
        ?.let { innerContext ->
            innerContext.flytting
                .filter { it.sluttDato.isAfter(context.sluttDato) }
                .map { flytting ->
                    createValidationReportEntry(
                        contextId = flytting.id,
                        messageText = "Flytting (${flytting.id}). Sluttdato (${flytting.sluttDato}) er " +
                                "etter individets sluttdato (${context.sluttDato})"
                    )
                }
                .ifEmpty { null }
        }
}