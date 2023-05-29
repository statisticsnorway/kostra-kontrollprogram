package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_EIGHTEEN
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_TWENTY_FOUR
import no.ssb.kostra.validation.rule.barnevern.extension.ageInYears

class Individ08 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_08.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.fodselsnummer
        ?.ageInYears(arguments.aargang.toInt())
        ?.takeIf { ageInYears ->
            ageInYears in AGE_EIGHTEEN..AGE_TWENTY_FOUR
                    && context.tiltak.none()
        }?.let {
            createSingleReportEntryList(
                journalId = context.journalnummer,
                contextId = context.id,
                messageText = "Individet er over 18 år og skal dermed ha tiltak"
            )
        }
}