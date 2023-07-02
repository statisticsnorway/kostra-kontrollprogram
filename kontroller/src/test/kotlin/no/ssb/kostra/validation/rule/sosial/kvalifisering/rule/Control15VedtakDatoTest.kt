package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VEDTAK_DATO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Control15VedtakDatoTest : BehaviorSpec({
    val sut = Control15VedtakDato()

    Given("context") {
        forAll(
            row(
                "reportingYear = currentYear, valid date",
                "0101${twoDigitReportingYear}",
                false
            ),
            row(
                "invalid vedtakDato",
                "a".repeat(6),
                true
            ),
            row(
                "5 year diff between reportingYear and vedtakDato",
                "0101${twoDigitReportingYear - 5}",
                true
            )
        ) { description, vedtakDate, expectError ->
            val context = kostraRecordInTest(vedtakDate)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet for 'Hvilken dato det ble fattet vedtak om " +
                            "program? (søknad innvilget)' med verdien ($vedtakDate) enten mangler utfylling, " +
                            "har ugyldig dato eller dato som er eldre enn 4 år fra rapporteringsåret " +
                            "($fourDigitReportingYear). Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(vedtakDateString: String) = kvalifiseringKostraRecordInTest(
            mapOf(VEDTAK_DATO_COL_NAME to vedtakDateString)
        )
    }
}
