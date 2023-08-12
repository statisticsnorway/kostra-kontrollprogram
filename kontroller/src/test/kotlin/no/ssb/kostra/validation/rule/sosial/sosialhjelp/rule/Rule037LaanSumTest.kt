package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_9_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule037LaanSumTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule037LaanSum(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "laan = 0, sum laan per måned = 0",
                kostraRecordInTest("0", "0"),
            ),
            ForAllRowItem(
                "laan = 1, sum laan per måned = 1",
                kostraRecordInTest("1", "1"),
            ),
            ForAllRowItem(
                "laan = 0, sum laan per måned = 1",
                kostraRecordInTest("0", "1"),
                expectedErrorMessage = "Det er ikke fylt ut lån (0) fordelt på måneder eller sum stemmer ikke med sum lån (1) utbetalt i løpet av året.",
            ),
            ForAllRowItem(
                "laan = X, sum laan per måned = 1",
                kostraRecordInTest(" ", "1"),
                expectedErrorMessage = "Det er ikke fylt ut lån (0) fordelt på måneder eller sum stemmer ikke med sum lån (1) utbetalt i løpet av året.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            laanMaaned: String,
            laan: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                LAAN_COL_NAME to laan,
                LAAN_1_COL_NAME to laanMaaned,
                LAAN_2_COL_NAME to " ",
                LAAN_3_COL_NAME to " ",
                LAAN_4_COL_NAME to " ",
                LAAN_5_COL_NAME to " ",
                LAAN_6_COL_NAME to " ",
                LAAN_7_COL_NAME to " ",
                LAAN_8_COL_NAME to " ",
                LAAN_9_COL_NAME to " ",
                LAAN_10_COL_NAME to " ",
                LAAN_11_COL_NAME to " ",
                LAAN_12_COL_NAME to " "
            )
        )
    }
}