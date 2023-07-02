package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VEDTAK_DATO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Control15VedtakDatoTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control15VedtakDato(),
            forAllRows = listOf(
                ForAllRowItem(
                    "reportingYear = currentYear, valid date",
                    kostraRecordInTest("0101${twoDigitReportingYear}")
                ),
                ForAllRowItem(
                    "invalid vedtakDato",
                    kostraRecordInTest("a".repeat(6)),
                    expectedErrorMessage = "Feltet for 'Hvilken dato det ble fattet vedtak om " +
                            "program? (søknad innvilget)' med verdien (${"a".repeat(6)}) enten mangler utfylling, " +
                            "har ugyldig dato eller dato som er eldre enn 4 år fra rapporteringsåret " +
                            "(${argumentsInTest.aargang}). Feltet er obligatorisk å fylle ut."
                ),
                ForAllRowItem(
                    "5 year diff between reportingYear and vedtakDato",
                    kostraRecordInTest("0101${twoDigitReportingYear - 5}"),
                    expectedErrorMessage = "Feltet for 'Hvilken dato det ble fattet vedtak om " +
                            "program? (søknad innvilget)' med verdien (0101${twoDigitReportingYear - 5}) enten " +
                            "mangler utfylling, har ugyldig dato eller dato som er eldre enn 4 år fra " +
                            "rapporteringsåret (${argumentsInTest.aargang}). Feltet er obligatorisk å fylle ut."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(vedtakDateString: String) = kvalifiseringKostraRecordInTest(
            mapOf(VEDTAK_DATO_COL_NAME to vedtakDateString)
        )
    }
}
