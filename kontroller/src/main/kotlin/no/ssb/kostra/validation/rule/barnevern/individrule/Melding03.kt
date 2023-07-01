package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Melding03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_03.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .filter {
            it.sluttDato != null
                    && it.sluttDato.isAfter(it.startDato.plusDays(MAX_DURATION_IN_DAYS))
        }
        .map { melding ->
            createValidationReportEntry(
                contextId = melding.id,
                messageText = "Melding (${melding.id}). Fristoverskridelse på behandlingstid for melding, " +
                        "(${melding.startDato} -> ${melding.sluttDato})"
            )
        }.ifEmpty { null }

    companion object {
        /** actual max duration in days from spec is 7 days, but 1 day grace period was requested by Bufdir */
        const val MAX_DURATION_IN_DAYS = 8L
    }
}