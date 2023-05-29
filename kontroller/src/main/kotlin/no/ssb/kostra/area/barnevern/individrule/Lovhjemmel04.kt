package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Lovhjemmel04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.LOVHJEMMEL_04.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak.filter {
        it.lovhjemmel.kapittel == "0"
                || it.lovhjemmel.paragraf == "0"
    }.flatMap {
        createSingleReportEntryList(
            journalId = context.journalnummer,
            contextId = it.id,
            messageText = "Tiltak (${it.id}). Kapittel (${it.lovhjemmel.kapittel}) eller paragraf " +
                    "(${it.lovhjemmel.paragraf}) er rapportert med den ugyldige koden 0"
        )
    }.ifEmpty { null }
}