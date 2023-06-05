package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_TWENTY_FOUR
import no.ssb.kostra.validation.rule.barnevern.extension.ageInYears

class Individ07 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_07.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) =
        context.fodselsnummer
            ?.ageInYears(arguments.aargang.toInt())
            ?.let { ageInYears ->
                if (ageInYears > AGE_TWENTY_FOUR) {
                    createSingleReportEntryList(
                        contextId = context.id,
                        messageText = "Individet er $ageInYears år og skal avsluttes som klient"
                    )
                } else null
            }
}
