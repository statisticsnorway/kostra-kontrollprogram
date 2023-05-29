package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.extension.erOmsorgsTiltak
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Lovhjemmel02 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.LOVHJEMMEL_02.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak.filter {
        it.erOmsorgsTiltak()
                && it.sluttDato != null
                && it.opphevelse == null
    }.flatMap { tiltak ->
        createSingleReportEntryList(
            journalId = context.journalnummer,
            contextId = tiltak.id,
            messageText = "Lovhjemmel Kontroll 2: Omsorgstiltak med sluttdato krever årsak til opphevelse"
        )
    }.ifEmpty { null }
}