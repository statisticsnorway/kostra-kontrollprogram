package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.SharedValidationConstants.AGE_TWENTY_FOUR
import no.ssb.kostra.area.barnevern.ValidationUtils.extractBirthDateFromSocialSecurityId
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Individ07 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_07.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) =
        context.fodselsnummer?.let { fodselsnummer ->
            extractBirthDateFromSocialSecurityId(fodselsnummer)?.let { dateOfBirth ->

                val ageInYears = ChronoUnit.YEARS.between(
                    dateOfBirth,
                    LocalDate.of(arguments.aargang.toInt(), 12, 31)
                )

                if (ageInYears > AGE_TWENTY_FOUR) {
                    createSingleReportEntryList(
                        journalId = context.journalnummer,
                        contextId = context.id,
                        messageText = "Individet er $ageInYears år og skal avsluttes som klient"
                    )
                } else null
            }
        }
}