package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule07AlderEr68AarEllerOver : AbstractRule<KostraRecord>(
    SosialRuleId.ALDER_ER_96_AAR_ELLER_OVER_07.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsTrimmedString(PERSON_FODSELSNR_COL_NAME).ageInYears(arguments.aargang.toInt())
            ?.takeIf { ageInYears -> ageInYears > 67 }?.let {
                createSingleReportEntryList(
                    messageText = "Deltakeren ($it år) er 68 år eller eldre."
                )
            }
}