package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ALDER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class AlderEr68AarEllerOver07 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.ALDER_ER_96_AAR_ELLER_OVER_07.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? =
        context.getFieldAsInteger(ALDER_COL_NAME)
            ?.takeIf { ageInYears -> ageInYears > 67 }
            ?.let {
                createSingleReportEntryList(
                    messageText = "Deltakeren ($it år) er 68 år eller eldre."
                )
            }
}