package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control36StatusForDeltakelseIKvalifiseringsprogramTest : BehaviorSpec({
    val sut = Control36StatusForDeltakelseIKvalifiseringsprogram()

    Given("context") {
        forAll(
            *(1..6).map {
                row(
                    "valid status, $it",
                    kostraRecordInTest(it.toString()),
                    false
                )
            }.toTypedArray(),
            row(
                "invalid status",
                kostraRecordInTest("7"),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér status. Fant '7', forventet én av"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(status: String) =
            kvalifiseringKostraRecordInTest(mapOf(STATUS_COL_NAME to status))
    }
}
