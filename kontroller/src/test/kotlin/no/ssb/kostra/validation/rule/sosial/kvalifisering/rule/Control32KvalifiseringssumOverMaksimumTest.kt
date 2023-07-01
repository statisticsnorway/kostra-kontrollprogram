package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control32KvalifiseringssumOverMaksimum.Companion.STONAD_SUM_MAX

class Control32KvalifiseringssumOverMaksimumTest : BehaviorSpec({
    val sut = Control32KvalifiseringssumOverMaksimum()

    Given("context") {
        forAll(
            row(
                "empty amount",
                kostraRecordInTest(" "),
                false
            ),
            row(
                "valid amount",
                kostraRecordInTest("42"),
                false
            ),
            row(
                "max amount",
                kostraRecordInTest(STONAD_SUM_MAX.toString()),
                false
            ),
            row(
                "amount too high",
                kostraRecordInTest((STONAD_SUM_MAX + 1).toString()),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Kvalifiseringsstønaden (600001) som deltakeren har fått i løpet av " +
                            "rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på NOK 600000,-."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(amount: String) = KostraRecord(
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_STONAD_COL_NAME to amount,
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
