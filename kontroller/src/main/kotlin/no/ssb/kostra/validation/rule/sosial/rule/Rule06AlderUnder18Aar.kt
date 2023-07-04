package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule06AlderUnder18Aar : AbstractRule<KostraRecord>(
    SosialRuleId.ALDER_UNDER_18_AAR_06.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.fieldAs<String>(PERSON_FODSELSNR_COL_NAME).ageInYears(arguments.aargang.toInt())
            ?.takeIf { ageInYears -> ageInYears < 18 }
            ?.let {
                createSingleReportEntryList(
                    messageText = "Deltakeren ($it år) er under 18 år."
                )
            }
}