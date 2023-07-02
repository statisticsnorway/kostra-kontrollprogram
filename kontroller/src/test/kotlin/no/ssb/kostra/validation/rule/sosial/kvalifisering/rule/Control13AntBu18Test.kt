package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BU18_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control13AntBu18Test : BehaviorSpec({
    val sut = Control13AntBu18()

    Given("context") {
        forAll(
            row(
                "numberOfChildren = 13",
                kostraRecordInTest(13),
                false
            ),
            row(
                "numberOfChildren = 14",
                kostraRecordInTest(14),
                true
            )
        ) { description, currentContext, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(currentContext, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Antall barn (14) under 18 år i husholdningen er 14 " +
                            "eller flere, er dette riktig?"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(numberOfChildren: Int) =
            kvalifiseringKostraRecordInTest(mapOf(ANT_BU18_COL_NAME to numberOfChildren.toString()))
    }
}
