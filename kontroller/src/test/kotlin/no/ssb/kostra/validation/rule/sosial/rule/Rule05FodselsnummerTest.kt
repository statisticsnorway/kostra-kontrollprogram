package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import java.time.LocalDate

class Rule05FodselsnummerTest : BehaviorSpec({
    val sut = Rule05Fodselsnummer()

    Given("context") {
        forAll(
            row(
                "record with valid fodselsnummer",
                kostraRecordInTest(generateRandomSSN(LocalDate.now().minusYears(1), LocalDate.now())),
                false
            ),
            row(
                "record with invalid fodselsnummer",
                kostraRecordInTest("42"),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(individId: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                PERSON_FODSELSNR_COL_NAME to individId
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
