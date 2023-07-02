package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control29KvalifiseringssumManglerTest : BehaviorSpec({
    val sut = Control29KvalifiseringssumMangler()

    Given("context") {
        forAll(
            row(
                "valid kvpStonad",
                kostraRecordInTest("1"),
                false
            ),
            row(
                "empty kvpStonad",
                kostraRecordInTest(" "),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                            "kvalifiseringsstønad ( ) i løpet av året, eller feltet inneholder andre tegn enn " +
                            "tall. Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(kvpStonad: String) =
            kvalifiseringKostraRecordInTest(mapOf(KVP_STONAD_COL_NAME to kvpStonad))
    }
}
