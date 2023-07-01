package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule09SivilstandTest : BehaviorSpec({
    val sut = Rule09Sivilstand()

    Given("context") {
        forAll(
            *(1..5).map {
                row(
                    "record with sivilstand = $it",
                    "$it",
                    false
                )
            }.toTypedArray(),
            row(
                "record with empty sivilstand",
                "", true
            ),
            row(
                "record with invalid sivilstand",
                "42", true
            )
        ) { description, maritalStatus, expectError ->
            val context = kostraRecordInTest(maritalStatus)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér sivilstand. Fant '$maritalStatus, forventet én av"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(maritalStatus: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                EKTSTAT_COL_NAME to maritalStatus
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
