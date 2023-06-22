package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Undersokelse07 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_07.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.konklusjon in codesThatRequiresLegalBasis
                    && it.vedtaksgrunnlag.none()
        }
        .map { undersokelse ->
            createValidationReportEntry(
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Undersøkelse konkludert med kode " +
                        "${undersokelse.konklusjon} skal ha vedtaksgrunnlag"
            )
        }.ifEmpty { null }

    companion object {
        val codesThatRequiresLegalBasis = setOf("1", "2")
    }
}