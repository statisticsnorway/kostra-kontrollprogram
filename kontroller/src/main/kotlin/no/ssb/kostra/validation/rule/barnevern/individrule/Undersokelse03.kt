package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Undersokelse03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_03.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.konklusjon == CODE_THAT_REQUIRES_CLARIFICATION
                    && it.presisering == null
        }
        .map { undersokelse ->
            createValidationReportEntry(
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Undersøkelse der kode for konklusjon " +
                        "er ${undersokelse.konklusjon} mangler presisering"
            )
        }.ifEmpty { null }


    companion object {
        const val CODE_THAT_REQUIRES_CLARIFICATION = "5"
    }
}