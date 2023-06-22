package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.extension.erOmsorgsTiltak

class Tiltak04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_04.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak
        .filter {
            it.sluttDato != null
                    && it.erOmsorgsTiltak()
                    && it.opphevelse == null
        }
        .map { tiltak ->
            createValidationReportEntry(
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Omsorgstiltak med sluttdato (${tiltak.sluttDato}) " +
                        "krever kode for opphevelse"
            )
        }.ifEmpty { null }
}