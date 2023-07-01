package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Control11Bu18AntBu18Test : BehaviorSpec({
    val sut = Control11Bu18AntBu18()

    Given("context") {
        forAll(
            row(
                "bu18Code = 0, numberOfChildren = 0",
                kostraRecordInTest(0, 0),
                false
            ),
            row(
                "bu18Code = 1, numberOfChildren = 1",
                kostraRecordInTest(1, 1),
                false
            ),
            row(
                "bu18Code = 0, numberOfChildren = 1",
                kostraRecordInTest(0, 1),
                false
            ),
            row(
                "bu18Code = 1, numberOfChildren = 0",
                kostraRecordInTest(1, 0),
                true
            )
        ) { description, currentContext, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(currentContext, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Det er krysset av for at det bor barn under 18 år i husholdningen " +
                            "som mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke " +
                            "oppgitt hvor mange barn '(0)' som bor i husholdningen. Feltet er obligatorisk å " +
                            "fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            bu18Code: Int,
            numberOfChildren: Int
        ) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                BU18_COL_NAME to bu18Code.toString(),
                ANT_BU18_COL_NAME to numberOfChildren.toString()

            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
