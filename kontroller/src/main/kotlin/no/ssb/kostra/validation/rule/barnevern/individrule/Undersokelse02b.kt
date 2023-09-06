package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Undersokelse02b : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_02B.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter {
            it.sluttDato?.let { sluttDato -> sluttDato.year != arguments.aargang.toInt() } ?: false
        }.map { undersokelse ->
            createValidationReportEntry(
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). Undersøkelsens sluttdato " +
                        "(${undersokelse.sluttDato}) er ikke i rapporteringsåret (${arguments.aargang})"
            )
        }.ifEmpty { null }
}
