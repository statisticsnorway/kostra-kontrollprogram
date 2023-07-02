package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control20KvalifiseringsprogramIAnnenKommuneKommunenummerTest : BehaviorSpec({
    val sut = Control20KvalifiseringsprogramIAnnenKommuneKommunenummer()

    Given("context") {
        forAll(
            row(
                "valid kvpKomm and kommnrKvpKomm",
                1,
                "1106",
                "N/A",
                false
            ),
            row(
                "valid kvpKomm and kommnrKvpKomm #2",
                2,
                "1106",
                "N/A",
                false
            ),
            row(
                "invalid kommnrKvpKomm",
                1,
                "4242",
                "Det er svart '1=Ja' på om deltakeren kommer fra " +
                        "kvalifiseringsprogram i annen kommune, men kommunenummer ('4242') mangler eller " +
                        "er ugyldig. Feltet er obligatorisk å fylle ut.",
                true
            ),
            row(
                "empty kommnrKvpKomm",
                1,
                " ",
                "Det er svart '1=Ja' på om deltakeren kommer fra " +
                        "kvalifiseringsprogram i annen kommune, men kommunenummer (' ') mangler eller " +
                        "er ugyldig. Feltet er obligatorisk å fylle ut.",
                true
            )
        ) { description, kvpKomm, kommnrKvpKomm, expectedErrorMessage, expectError ->
            val context = kostraRecordInTest(kvpKomm, kommnrKvpKomm)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    expectedErrorMessage
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            kvpKomm: Int,
            kommnrKvpKomm: String
        ) = kvalifiseringKostraRecordInTest(
            mapOf(
                KVP_KOMM_COL_NAME to kvpKomm.toString(),
                KOMMNR_KVP_KOMM_COL_NAME to kommnrKvpKomm
            )
        )
    }
}
