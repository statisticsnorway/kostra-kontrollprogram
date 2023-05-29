package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.extension.erOmsorgsTiltak
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_04.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak
        .filter {
            it.sluttDato != null
                    && it.erOmsorgsTiltak()
                    && it.opphevelse == null
        }
        .map { tiltak ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Omsorgstiltak med sluttdato (${tiltak.sluttDato}) " +
                        "krever kode for opphevelse"
            )
        }.ifEmpty { null }
}