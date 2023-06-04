package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Undersokelse02b : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_02B.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.sluttDato != null
                    && it.sluttDato.year != arguments.aargang.toInt()
        }.map { undersokelse ->
            createValidationReportEntry(
                journalId = context.journalnummer,
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Undersøkelsens sluttdato " +
                        "(${undersokelse.sluttDato}) er ikke i rapporteringsåret (${arguments.aargang})"
            )
        }.ifEmpty { null }
}
