package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import java.time.Year

object KvalifiseringTestUtils {

    fun kvalifiseringKostraRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = KostraRecord(
        valuesByName = valuesByName
            .plus(KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion())
            .plus(VERSION_COL_NAME to twoDigitReportingYear.toString()),
        fieldDefinitionByName = KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
    )

    val twoDigitReportingYear = (Year.now().value - 1) % 1000
}