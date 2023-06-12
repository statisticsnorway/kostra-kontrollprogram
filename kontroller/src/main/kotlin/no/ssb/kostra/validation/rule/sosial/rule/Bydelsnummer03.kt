package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.DISTRICT_ID_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.extension.districtIdFromRegion
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Bydelsnummer03 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.BYDELSNUMMER_03.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {

        val districtId = context.getFieldAsTrimmedString(DISTRICT_ID_COL_NAME)
        val districtIdFromRegion = arguments.region.districtIdFromRegion()

        return if (districtId != districtIdFromRegion) {
            createSingleReportEntryList(
                "Korrigér bydel. Fant $districtId, forventet $districtIdFromRegion"
            )
        } else null
    }
}