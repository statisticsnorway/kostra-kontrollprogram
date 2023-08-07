package no.ssb.kostra.validation.rule.sosial.sosialhjelp

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.RuleTestData

object SosialhjelpTestUtils {

    fun sosialKostraRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = listOf(
        KostraRecord(
            valuesByName = emptyMap<String, String>()
                .plus(SosialColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion())
                .plus(SosialColumnNames.VERSION_COL_NAME to twoDigitReportingYear.toString())
                .plus(KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler")
                .plus(KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalId~")
                .plus (KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to "~fodselsnr~")
                .plus(valuesByName)
            ,
            fieldDefinitionByName = SosialFieldDefinitions.fieldDefinitionsByName
        )
    )

    val twoDigitReportingYear = RuleTestData.argumentsInTest.aargang.toInt() % 1000
}