package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.UTBETDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils.twoDigitReportingYear

class Rule041DatoForUtbetalingsvedtakTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule041DatoForUtbetalingsvedtak(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vilkar = X, utbetdato = blank",
                    kostraRecordInTest("X", "")
                ),
                ForAllRowItem(
                    "vilkar = 1, utbetdato = 0101$twoDigitReportingYear",
                    kostraRecordInTest("1", "0101$twoDigitReportingYear")
                ),
                ForAllRowItem(
                    "vilkar = 1, utbetdato = blank",
                    kostraRecordInTest("1", "      "),
                    expectedErrorMessage = "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter " +
                            "sosialtjenesteloven', så skal utbetalingsvedtakets dato (      ) oppgis. " +
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
            utbetDato: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VILKARSOSLOV_COL_NAME to vilkar,
                UTBETDATO_COL_NAME to utbetDato
            )
        )
    }
}