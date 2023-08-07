package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
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
        valuesByName = emptyMap<String, String>()
            .plus(KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion())
            .plus(VERSION_COL_NAME to twoDigitReportingYear.toString())
            .plus(SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler")
            .plus(PERSON_JOURNALNR_COL_NAME to "~journalId~")
            .plus (PERSON_FODSELSNR_COL_NAME to "~fodselsnr~")
            .plus(valuesByName),
        fieldDefinitionByName = KvalifiseringFieldDefinitions.fieldDefinitions.associateBy { it.name }
    )

    val twoDigitReportingYear = (Year.now().value - 1) % 1000
}