package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Melding04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_04.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.melding
        .filter {
            it.sluttDato != null
                    && arguments.aargang.toInt() - 1 < it.sluttDato.year
                    && it.konklusjon != null
                    && it.konklusjon in setOf("1", "2")
                    && it.melder.none()
        }
        .map { melding ->
            createValidationReportEntry(
                journalId = context.journalnummer,
                contextId = melding.id,
                messageText = "Melding (${melding.id}). Konkludert melding mangler melder(e)."
            )
        }.ifEmpty { null }
}