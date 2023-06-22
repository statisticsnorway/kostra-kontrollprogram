package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Tiltak02a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_02A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak
        .filter {
            it.sluttDato != null
                    && it.startDato.isAfter(it.sluttDato)
        }.map { tiltak ->
            createValidationReportEntry(
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Startdato (${tiltak.startDato}) for tiltaket er " +
                        "etter sluttdato (${tiltak.sluttDato}) for tiltaket"
            )
        }.ifEmpty { null }
}
