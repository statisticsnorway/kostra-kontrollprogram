package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Undersokelse02c : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_02C.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context
        .takeIf { it.sluttDato != null }
        ?.let { innerContext ->
            innerContext.melding
                .mapNotNull { it.undersokelse }
                .filter {
                    it.sluttDato != null
                            && it.sluttDato.isAfter(context.sluttDato)
                }.map { undersokelse ->
                    createValidationReportEntry(
                        journalId = innerContext.journalnummer,
                        contextId = undersokelse.id,
                        messageText = "Undersøkelse (${undersokelse.id}). Undersøkelsens sluttdato " +
                                "(${undersokelse.sluttDato}) er etter individets sluttdato (${innerContext.sluttDato})"
                    )
                }.ifEmpty { null }
        }
}