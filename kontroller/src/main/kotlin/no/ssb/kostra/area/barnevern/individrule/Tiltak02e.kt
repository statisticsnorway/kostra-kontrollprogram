package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak02e : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_02E.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak
        .filter { it.startDato.isBefore(context.startDato) }
        .map { tiltak ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). StartDato (${tiltak.startDato}) skal være lik eller etter " +
                        "individets startdato (${context.startDato})"
            )
        }.ifEmpty { null }
}