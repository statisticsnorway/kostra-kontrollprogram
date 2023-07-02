package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control19KvalifiseringsprogramIAnnenKommuneTest : BehaviorSpec({
    val sut = Control19KvalifiseringsprogramIAnnenKommune()

    Given("context") {
        forAll(
            row(
                "code = 1",
                1,
                false
            ),
            row(
                "code = 2",
                2,
                false
            ),
            row(
                "invalid code",
                42,
                true
            )
        ) { description, code, expectError ->
            val context = kostraRecordInTest(code)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet for 'Kommer deltakeren fra kvalifiseringsprogram i " +
                            "annen kommune?' er ikke fylt ut, eller feil kode er benyttet ($code). " +
                            "Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(code: Int) =
            kvalifiseringKostraRecordInTest(mapOf(KVP_KOMM_COL_NAME to code.toString()))
    }
}
