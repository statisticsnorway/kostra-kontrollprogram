package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Kommunenummer03 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KOMMUNENUMMER_03.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {

        val municipalityId = context.getFieldAsTrimmedString(MUNICIPALITY_ID_COL_NAME)
        val municipalityIdFromRegion = arguments.region.municipalityIdFromRegion()

        return if (municipalityId != municipalityIdFromRegion) {
            createSingleReportEntryList(
                "Korrigér kommunenummeret. Fant $municipalityId, forventet $municipalityIdFromRegion"
            )
        } else null
    }
}