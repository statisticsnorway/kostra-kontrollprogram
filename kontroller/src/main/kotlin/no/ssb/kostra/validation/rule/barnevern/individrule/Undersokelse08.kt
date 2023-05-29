package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import java.time.LocalDate

class Undersokelse08 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_08.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.startDato.isBefore(LocalDate.of(arguments.aargang.toInt(), 6, 30))
                    && it.sluttDato == null
        }
        .map { undersokelse ->
            createValidationReportEntry(
                journalId = context.journalnummer,
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Undersøkelsen startet ${undersokelse.startDato} " +
                        "og skal konkluderes da den har pågått i mer enn 6 måneder"
            )
        }.ifEmpty { null }
}