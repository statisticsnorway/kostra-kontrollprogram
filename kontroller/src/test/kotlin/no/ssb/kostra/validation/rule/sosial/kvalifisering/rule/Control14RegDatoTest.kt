package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Control14RegDatoTest : BehaviorSpec({
    val sut = Control14RegDato()

    Given("context") {
        forAll(
            row(
                "reportingYear = currentYear, valid date",
                "0101${twoDigitReportingYear}",
                false
            ),
            row(
                "5 year diff between reportingYear and regDato",
                "0101${twoDigitReportingYear - 5}",
                true
            ),
            row(
                "invalid regDato",
                "a".repeat(6),
                true
            )
        ) { description, regDato, expectError ->
            val context = kostraRecordInTest(regDato)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                            "med verdien ($regDato) enten mangler utfylling, har ugyldig dato eller dato som er " +
                            "eldre enn 4 år fra rapporteringsåret ($fourDigitReportingYear). " +
                            "Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(regDatoString: String) = kvalifiseringKostraRecordInTest(
            mapOf(REG_DATO_COL_NAME to regDatoString)
        )
    }
}
