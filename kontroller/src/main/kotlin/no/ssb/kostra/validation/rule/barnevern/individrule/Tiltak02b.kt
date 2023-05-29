package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak02b : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_02B.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak
        .filter {
            it.sluttDato != null
                    && it.sluttDato.year != arguments.aargang.toInt()
        }.map { plan ->
            createValidationReportEntry(
                journalId = context.journalnummer,
                contextId = plan.id,
                messageText = "Tiltak (${plan.id}). Sluttdato (${plan.sluttDato}) er ikke " +
                        "i rapporteringsåret (${arguments.aargang})"
            )
        }.ifEmpty { null }
}
