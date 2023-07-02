package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Control14RegDatoTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control14RegDato(),
            forAllRows = listOf(
                ForAllRowItem(
                    "reportingYear = currentYear, valid date",
                    kostraRecordInTest("0101${twoDigitReportingYear}")
                ),
                ForAllRowItem(
                    "5 year diff between reportingYear and regDato",
                    kostraRecordInTest("0101${twoDigitReportingYear - 5}"),
                    expectedErrorMessage = "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                            "med verdien (${"0101${twoDigitReportingYear - 5}"}) enten mangler utfylling, har ugyldig " +
                            "dato eller dato som er eldre enn 4 år fra rapporteringsåret ($fourDigitReportingYear). " +
                            "Feltet er obligatorisk å fylle ut."
                ),
                ForAllRowItem(
                    "invalid regDato",
                    kostraRecordInTest("a".repeat(6)),
                    expectedErrorMessage = "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                            "med verdien (${"a".repeat(6)}) enten mangler utfylling, har ugyldig dato eller dato " +
                            "som er eldre enn 4 år fra rapporteringsåret ($fourDigitReportingYear). " +
                            "Feltet er obligatorisk å fylle ut."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(regDatoString: String) = kvalifiseringKostraRecordInTest(
            mapOf(REG_DATO_COL_NAME to regDatoString)
        )
    }
}
