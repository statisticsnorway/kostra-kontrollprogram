package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule08KjonnTest : BehaviorSpec({
    val sut = Rule08Kjonn()

    Given("context") {
        forAll(
            row(
                "record with kjonn = MANN",
                "1", false
            ),
            row(
                "record with kjonn = KVINNE",
                "2", false
            ),
            row(
                "record with empty kjonn",
                "", true
            ),
            row(
                "record with invalid kjonn",
                "42", true
            )
        ) { description, gender, expectError ->
            val context = kostraRecordInTest(gender)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér kjønn. Fant '$gender', forventet én av"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(genderId: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KJONN_COL_NAME to genderId
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
