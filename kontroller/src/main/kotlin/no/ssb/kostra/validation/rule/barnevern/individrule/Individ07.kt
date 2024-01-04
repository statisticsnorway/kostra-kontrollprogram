package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_TWENTY_FIVE

class Individ07 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_07.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) =
        context.also { println(arguments) }.fodselsnummer
            ?.ageInYears(arguments.aargang.toInt())
            ?.let { ageInYears ->
                if (ageInYears >= AGE_TWENTY_FIVE) {
                    createSingleReportEntryList(
                        contextId = context.id,
                        messageText = "Individet er $ageInYears år og skal avsluttes som klient"
                    )
                } else null
            }
}
