package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.UTBETDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils.twoDigitReportingYear

class Rule041DatoForUtbetalingsvedtakTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule041DatoForUtbetalingsvedtak(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "vilkar = X, utbetdato = blank",
                kostraRecordInTest("X", ""),
            ),
            ForAllRowItem(
                "vilkar = 1, utbetdato = 010120$twoDigitReportingYear",
                kostraRecordInTest("1", "010120$twoDigitReportingYear"),
            ),
            ForAllRowItem(
                "vilkar = 1, utbetdato = blank",
                kostraRecordInTest("1", "        "),
                expectedErrorMessage = "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter " +
                        "sosialtjenesteloven', så skal utbetalingsvedtakets dato (        ) oppgis. " +
                        "Feltet er obligatorisk å fylle ut.",
            )
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