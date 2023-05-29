package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak02a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_02A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak
        .filter {
            it.sluttDato != null
                    && it.startDato.isAfter(it.sluttDato)
        }.map { tiltak ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Startdato (${tiltak.startDato}) for tiltaket er " +
                        "etter sluttdato (${tiltak.sluttDato}) for tiltaket"
            )
        }.ifEmpty { null }
}
