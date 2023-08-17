package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Undersokelse04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_04.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.sluttDato != null
                    && it.konklusjon == null
        }
        .map { undersokelse ->
            createValidationReportEntry(
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Avsluttet undersøkelse mangler konklusjon"
            )
        }.ifEmpty { null }
}