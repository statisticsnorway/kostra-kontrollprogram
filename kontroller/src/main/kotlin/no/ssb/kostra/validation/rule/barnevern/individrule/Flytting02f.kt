package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Flytting02f : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.FLYTTING_02F.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.flytting
        .filter { it.sluttDato.isBefore(context.startDato) }
        .map { flytting ->
            createValidationReportEntry(
                contextId = flytting.id,
                messageText = "Flytting (${flytting.id}). Sluttdato (${flytting.sluttDato}) er " +
                        "før individets startdato (${context.startDato})"
            )
        }
        .ifEmpty { null }
}