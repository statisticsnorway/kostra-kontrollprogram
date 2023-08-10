package no.ssb.kostra.validation.rule.sosial.sosialhjelp

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

object SosialhjelpTestUtils {

    fun sosialKostraRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = listOf(
        " ".repeat(fieldDefinitions.last().to)
            .toKostraRecord(index = 1, fieldDefinitions)
            .plus(KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion())
            .plus(VERSION_COL_NAME to twoDigitReportingYear.toString())
            .plus(SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler")
            .plus(PERSON_JOURNALNR_COL_NAME to "~journalId~")
            .plus(PERSON_FODSELSNR_COL_NAME to "~fodselsnr~")
            .plus(valuesByName)
    )

    val twoDigitReportingYear = argumentsInTest.aargang.toInt() % 1000
}