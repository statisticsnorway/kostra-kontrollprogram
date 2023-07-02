package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_VIKTIGSTE_INNTEKT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear

class Control39FullforteAvsluttedeProgramInntektkildeTest : BehaviorSpec({
    val sut = Control39FullforteAvsluttedeProgramInntektkilde()

    Given("context") {
        forAll(
            row(
                "status != 3",
                kostraRecordInTest("1", " "),
                false
            ),
            row(
                "status = 3, income code defined",
                kostraRecordInTest("3", "01"),
                false
            ),
            row(
                "status = 3, no income code defined",
                kostraRecordInTest("3", "  "),
                true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet 'Hva var deltakerens <b>viktigste</b> inntektskilde " +
                            "umiddelbart etter avslutningen? Må fylles ut dersom det er krysset av for " +
                            "svaralternativ 3 = Deltakeren har fullført program eller avsluttet program etter " +
                            "avtale (gjelder ikke flytting) under feltet for 'Hva er status for deltakelsen i " +
                            "kvalifiseringsprogrammet per 31.12.${fourDigitReportingYear}'?"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            status: String,
            inntekt: String
        ) = KvalifiseringTestUtils.kvalifiseringKostraRecordInTest(
            mapOf(
                STATUS_COL_NAME to status,
                AVSL_VIKTIGSTE_INNTEKT_COL_NAME to inntekt,
            )
        )
    }
}
