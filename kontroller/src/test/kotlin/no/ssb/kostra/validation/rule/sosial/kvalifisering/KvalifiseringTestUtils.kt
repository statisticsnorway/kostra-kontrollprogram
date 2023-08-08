package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import java.time.Year

object KvalifiseringTestUtils {

    fun kvalifiseringKostraRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = " ".repeat(fieldDefinitions.last().to)
        .toKostraRecord(index = 1, fieldDefinitions)
        .plus(KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion())
        .plus(VERSION_COL_NAME to twoDigitReportingYear.toString())
        .plus(SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler")
        .plus(PERSON_JOURNALNR_COL_NAME to "~journalId~")
        .plus(PERSON_FODSELSNR_COL_NAME to "~fodselsnr~")
        .plus(valuesByName)

    val twoDigitReportingYear = (Year.now().value - 1) % 1000
}