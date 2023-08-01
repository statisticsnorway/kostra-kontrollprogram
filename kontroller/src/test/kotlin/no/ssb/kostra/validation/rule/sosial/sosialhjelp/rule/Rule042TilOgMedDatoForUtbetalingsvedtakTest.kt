package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.UTBETTOMDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule042TilOgMedDatoForUtbetalingsvedtakTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule042TilOgMedDatoForUtbetalingsvedtak(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vilkar = X, utbettomdato = blank",
                    kostraRecordInTest("X", "")
                ),
                ForAllRowItem(
                    "vilkar = 1, utbettomdato = 0101${SosialhjelpTestUtils.twoDigitReportingYear}",
                    kostraRecordInTest("1", "0101${SosialhjelpTestUtils.twoDigitReportingYear}")
                ),
                ForAllRowItem(
                    "vilkar = 1, utbettomdato = blank",
                    kostraRecordInTest("1", "      "),
                    expectedErrorMessage = "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter " +
                            "sosialtjenesteloven', så skal utbetalingsvedtakets til og med dato (      ) oppgis. " +
                            "Feltet er obligatorisk å fylle ut."
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vilkar: String,
            utbetTomDato: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VILKARSOSLOV_COL_NAME to vilkar,
                UTBETTOMDATO_COL_NAME to utbetTomDato
            )
        )
    }
}