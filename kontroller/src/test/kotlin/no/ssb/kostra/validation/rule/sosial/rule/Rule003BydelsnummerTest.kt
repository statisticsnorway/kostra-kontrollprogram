package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest

class Rule003BydelsnummerTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule003Bydelsnummer(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "record with Oslo municipality and valid bydel",
                kostraRecordInTest(municipalityId = OSLO_MUNICIPALITY_ID, districtId = "01"),
            ),
            ForAllRowItem(
                "record not with Oslo municipality and blank bydel",
                kostraRecordInTest(municipalityId = "1234", districtId = "  "),
            ),
            ForAllRowItem(
                "record with Oslo municipality and blank bydel",
                kostraRecordInTest(municipalityId = OSLO_MUNICIPALITY_ID, districtId = "  "),
                expectedErrorMessage = "Korrigér bydel. Fant   , forventet en av [01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 13, 14, 15].",
            ),
            ForAllRowItem(
                "record with Oslo municipality and invalid bydel",
                kostraRecordInTest(municipalityId = OSLO_MUNICIPALITY_ID, districtId = "42"),
                expectedErrorMessage = "Korrigér bydel. Fant 42, forventet en av [01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 13, 14, 15].",
            ),
            ForAllRowItem(
                "record not with Oslo municipality and with bydel",
                kostraRecordInTest(municipalityId = "1234", districtId = "42"),
                expectedErrorMessage = "Korrigér bydel. Fant 42, forventet blank / '  '.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(municipalityId: String, districtId: String) =
            listOf(
                mapOf(
                    KOMMUNE_NR_COL_NAME to municipalityId,
                    BYDELSNR_COL_NAME to districtId,
                    PERSON_FODSELSNR_COL_NAME to "~fodselsnr~",
                    SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                    PERSON_JOURNALNR_COL_NAME to "~journalId~"

                ).toKostraRecord(lineNumber = 1, fieldDefinitions = KvalifiseringFieldDefinitions.fieldDefinitions)
            )
    }
}
