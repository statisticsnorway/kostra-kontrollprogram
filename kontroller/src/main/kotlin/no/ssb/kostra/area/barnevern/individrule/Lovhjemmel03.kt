package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.SharedValidationConstants
import no.ssb.kostra.area.barnevern.extension.ageInYears
import no.ssb.kostra.area.barnevern.extension.erOmsorgsTiltak
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Lovhjemmel03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.LOVHJEMMEL_03.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.fodselsnummer
        ?.ageInYears(arguments.aargang.toInt())
        ?.takeIf { ageInYears ->
            ageInYears in SharedValidationConstants.AGE_EIGHTEEN..SharedValidationConstants.AGE_TWENTY_FOUR
                    && context.tiltak.any { it.erOmsorgsTiltak() }
        }?.let { ageInYears ->
            context.tiltak
                .filter { it.erOmsorgsTiltak() }
                .flatMap {
                    createSingleReportEntryList(
                        journalId = context.journalnummer,
                        contextId = it.id,
                        messageText = "Tiltak (${it.id}). Individet er $ageInYears år og skal dermed ikke ha omsorgstiltak"
                    )
                }
        }
}