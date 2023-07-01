package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.testutil.RandomUtils.generateRandomSsn
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule07AlderEr68AarEllerOverTest : BehaviorSpec({
    val sut = Rule07AlderEr68AarEllerOver()

    Given("context") {
        forAll(
            row(
                "record with valid age",
                generateRandomSsn(67),
                false
            ),
            row(
                "record with blank fødselsnummer",
                " ".repeat(11),
                false
            ),
            row(
                "record with invalid age",
                generateRandomSsn(69),
                true
            )
        ) { description, foedselsnummer, expectError ->
            val context = kostraRecordInTest(foedselsnummer)
            val actualAge = foedselsnummer.ageInYears(argumentsInTest.aargang.toInt())

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Deltakeren ($actualAge år) er 68 år eller eldre."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(foedselsnummer: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                PERSON_FODSELSNR_COL_NAME to foedselsnummer
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
