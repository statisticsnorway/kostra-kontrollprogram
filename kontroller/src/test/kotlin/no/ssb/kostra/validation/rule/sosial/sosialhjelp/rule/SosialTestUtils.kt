package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.Year

object SosialTestUtils {

    fun sosialKostraRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = listOf(
        KostraRecord(
            valuesByName = valuesByName
                .plus(SosialColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion())
                .plus(SosialColumnNames.VERSION_COL_NAME to twoDigitReportingYear.toString()),
            fieldDefinitionByName = SosialFieldDefinitions.fieldDefinitionsByName
        )
    )

    val twoDigitReportingYear = (Year.now().value - 1) % 1000
}