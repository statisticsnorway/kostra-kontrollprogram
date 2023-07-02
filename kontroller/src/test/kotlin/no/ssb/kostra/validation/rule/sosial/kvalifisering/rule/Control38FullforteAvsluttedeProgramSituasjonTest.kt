package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ORDINAERTARB_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control38FullforteAvsluttedeProgramSituasjon.Companion.qualifyingFields

class Control38FullforteAvsluttedeProgramSituasjonTest : BehaviorSpec({
    val sut = Control38FullforteAvsluttedeProgramSituasjon()

    Given("context") {
        forAll(
            row(
                "no qualifying fields matches",
                kostraRecordInTest("1", " "),
                false
            ),
            row(
                "one qualifying fields matches, status != 3",
                kostraRecordInTest("1", "1"),
                false
            ),
            row(
                "one qualifying fields matches, status = 3",
                kostraRecordInTest("3", "01"),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet 'Ved fullført program eller program avsluttet etter " +
                            "avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter " +
                            "avslutningen'? Må fylles ut dersom det er krysset av for svaralternativ 3 = " +
                            "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke " +
                            "flytting) under feltet for 'Hva er status for deltakelsen i " +
                            "kvalifiseringsprogrammet per 31.12.${fourDigitReportingYear}'?"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            status: String,
            avslOrdinaertarb: String
        ) = kvalifiseringKostraRecordInTest(
            mapOf(
                STATUS_COL_NAME to status,
                AVSL_ORDINAERTARB_COL_NAME to avslOrdinaertarb,
                *(qualifyingFields.filter { it != AVSL_ORDINAERTARB_COL_NAME }.map { it to " " }).toTypedArray()
            )
        )
    }
}
