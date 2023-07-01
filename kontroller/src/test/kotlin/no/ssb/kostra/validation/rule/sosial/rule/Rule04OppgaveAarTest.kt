package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule04OppgaveAarTest : BehaviorSpec({
    val sut = Rule04OppgaveAar()

    Given("context") {
        forAll(
            row(
                "record with valid aargang",
                (argumentsInTest.aargang.toInt() - 2_000).toString(),
                false
            ),
            row(
                "record with non-numeric aargang",
                "ab",
                true
            ),
            row(
                "record with invalid aargang",
                "42",
                true
            )
        ) { description, aargang, expectError ->
            val context = kostraRecordInTest(aargang)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér årgang. Fant $aargang, forventet"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(version: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                VERSION_COL_NAME to version
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
