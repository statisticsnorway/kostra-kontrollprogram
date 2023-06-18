package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ALDER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule06AlderUnder18Aar : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.ALDER_UNDER_18_AAR_06.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsInteger(ALDER_COL_NAME)
            ?.takeIf { ageInYears -> ageInYears < 18 }
            ?.let {
                createSingleReportEntryList(
                    messageText = "Deltakeren ($it år) er under 18 år."
                )
            }
}