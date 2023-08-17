package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Lovhjemmel04 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.LOVHJEMMEL_04.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak.filter {
        it.lovhjemmel.kapittel == "0"
                || it.lovhjemmel.paragraf == "0"
    }.map {
        createValidationReportEntry(
            contextId = it.id,
            messageText = "Tiltak (${it.id}). Kapittel (${it.lovhjemmel.kapittel}) eller paragraf " +
                    "(${it.lovhjemmel.paragraf}) er rapportert med den ugyldige koden 0"
        )
    }.ifEmpty { null }
}