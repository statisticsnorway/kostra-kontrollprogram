package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Melding03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_03.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.melding
        .filter {
            it.sluttDato != null
                    && it.sluttDato.isAfter(it.startDato.plusDays(MAX_DURATION_IN_DAYS))
        }
        .map { melding ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = melding.id,
                messageText = "Melding (${melding.id}). Fristoverskridelse på behandlingstid for melding,  " +
                        "(${melding.startDato} -> ${melding.sluttDato})"
            )
        }.ifEmpty { null }

    companion object {
        /** actual max duration in days from spec is 7 days, but 1 day grace period was requested by Bufdir */
        const val MAX_DURATION_IN_DAYS = 8L
    }
}