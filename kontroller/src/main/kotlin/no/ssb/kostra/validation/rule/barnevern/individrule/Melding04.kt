package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Melding04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_04.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .filter {
            it.sluttDato?.let { sluttDato ->
                arguments.aargang.toInt() - 1 < sluttDato.year
                        && it.konklusjon != null
                        && it.konklusjon in setOf("1", "2")
                        && it.melder.none()
            } ?: false
        }
        .map { melding ->
            createValidationReportEntry(
                contextId = melding.id,
                messageText = "Melding (${melding.id}). Konkludert melding mangler melder(e)."
            )
        }.ifEmpty { null }
}