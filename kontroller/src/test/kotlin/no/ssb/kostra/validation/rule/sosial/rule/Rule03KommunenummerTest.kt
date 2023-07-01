package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule03KommunenummerTest : BehaviorSpec({
    val sut = Rule03Kommunenummer()

    Given("context") {
        forAll(
            row(
                "record with valid kommunenummer",
                kostraRecordInTest(argumentsInTest.region.municipalityIdFromRegion()),
                false
            ),
            row(
                "record with invalid kommunenummer",
                kostraRecordInTest("4242"),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér kommunenummeret. Fant 4242, " +
                            "forventet ${argumentsInTest.region.municipalityIdFromRegion()}."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(municipalityId: String) = KostraRecord(
            1,
            mapOf(
                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Sak",
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to "123",
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to "19096632188",
                KvalifiseringColumnNames.STATUS_COL_NAME to "1",
                KvalifiseringColumnNames.AVSL_DATO_COL_NAME to "010120",
                KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME to municipalityId
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
