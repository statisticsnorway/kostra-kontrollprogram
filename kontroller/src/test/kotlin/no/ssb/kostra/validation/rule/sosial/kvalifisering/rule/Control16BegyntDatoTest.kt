package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Control16BegyntDatoTest : BehaviorSpec({
    val sut = Control16BegyntDato()

    Given("context") {
        forAll(
            row(
                "reportingYear = currentYear, valid date",
                "0101$twoDigitReportingYear",
                false
            ),
            row(
                "5 year diff between reportingYear and vedtakDato",
                "0101${twoDigitReportingYear - 5}",
                true
            ),
            row(
                "invalid begyntDato",
                "a".repeat(6),
                true
            )
        ) { description, begyntDato, expectError ->
            val context = kostraRecordInTest(begyntDato)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet for 'Hvilken dato begynte deltakeren i program? " +
                            "(iverksettelse)' med verdien ($begyntDato) enten mangler utfylling, har ugyldig dato " +
                            "eller dato som er eldre enn 4 år fra rapporteringsåret ($fourDigitReportingYear). " +
                            "Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(begyntDateString: String) = kvalifiseringKostraRecordInTest(
            mapOf(BEGYNT_DATO_COL_NAME to begyntDateString)
        )
    }
}
