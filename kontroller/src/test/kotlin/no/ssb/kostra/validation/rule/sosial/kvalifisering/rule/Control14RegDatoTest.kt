package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Control14RegDatoTest : BehaviorSpec({
    val sut = Control14RegDato()

    Given("context") {
        forAll(
            row(
                "reportingYear = currentYear, valid date",
                "010122",
                false
            ),
            row(
                "6 year diff between reportingYear and regDato",
                "010116",
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
                            "eldre enn 4 år fra rapporteringsåret (2022). Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            regDatoString: String
        ) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                VERSION_COL_NAME to "22",
                REG_DATO_COL_NAME to regDatoString

            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
