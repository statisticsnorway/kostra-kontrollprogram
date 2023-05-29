package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.SharedValidationConstants.koderSomKreverPresisering
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak07 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_07.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak
        .filter {
            it.kategori.kode in koderSomKreverPresisering
                    && it.kategori.presisering == null
        }.map { tiltak ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Tiltakskategori (${tiltak.kategori.kode}) mangler presisering"
            )
        }.ifEmpty { null }
}
