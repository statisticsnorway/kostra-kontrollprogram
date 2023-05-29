package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Undersokelse03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_03.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.konklusjon == CODE_THAT_REQUIRES_CLARIFICATION
                    && it.presisering == null
        }
        .map { undersokelse ->
            createValidationReportEntry(
                journalId = context.journalnummer,
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Undersøkelse der kode for konklusjon " +
                        "er ${undersokelse.konklusjon} mangler presisering"
            )
        }.ifEmpty { null }


    companion object {
        const val CODE_THAT_REQUIRES_CLARIFICATION = "5"
    }
}