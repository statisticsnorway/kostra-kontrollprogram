package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.koderSomKreverPresisering

class Tiltak07 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_07.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak
        .filter {
            it.kategori.kode in koderSomKreverPresisering
                    && it.kategori.presisering == null
        }.map { tiltak ->
            createValidationReportEntry(
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Tiltakskategori (${tiltak.kategori.kode}) mangler presisering"
            )
        }.ifEmpty { null }
}
