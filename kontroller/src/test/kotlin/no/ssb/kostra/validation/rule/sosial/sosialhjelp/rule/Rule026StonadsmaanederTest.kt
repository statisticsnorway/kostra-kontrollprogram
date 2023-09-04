package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_9_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule026StonadsmaanederTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule026Stonadsmaaneder(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "varighet = 01",
                kostraRecordInTest("01"),
            ),
            ForAllRowItem(
                "varighet = XX",
                kostraRecordInTest("XX"),
                expectedErrorMessage = "Det er ikke krysset av for hvilke måneder mottakeren har fått utbetalt " +
                        "økonomisk sosialhjelp (bidrag (1000) eller lån (0)) i løpet av rapporteringsåret. " +
                        "Feltet er obligatorisk å fylle ut.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            varighet: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                BIDRAG_COL_NAME to "1000",
                LAAN_COL_NAME to "0",
                STMND_1_COL_NAME to varighet,
                STMND_2_COL_NAME to " ",
                STMND_3_COL_NAME to " ",
                STMND_4_COL_NAME to " ",
                STMND_5_COL_NAME to " ",
                STMND_6_COL_NAME to " ",
                STMND_7_COL_NAME to " ",
                STMND_8_COL_NAME to " ",
                STMND_9_COL_NAME to " ",
                STMND_10_COL_NAME to " ",
                STMND_11_COL_NAME to " ",
                STMND_12_COL_NAME to " ",

                )
        )
    }
}