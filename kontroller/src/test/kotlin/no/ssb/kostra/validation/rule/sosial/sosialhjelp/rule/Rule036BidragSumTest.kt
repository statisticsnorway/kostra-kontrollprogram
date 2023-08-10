package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_9_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule036BidragSumTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule036BidragSum(),
            forAllRows = listOf(
                ForAllRowItem(
                    "bidrag = 0, sum bidrag per måned = 0",
                    kostraRecordInTest("0", "0"),
                ),
                ForAllRowItem(
                    "bidrag = 1, sum bidrag per måned = 1",
                    kostraRecordInTest("1", "1"),
                ),
                ForAllRowItem(
                    "bidrag = 0, sum bidrag per måned = 1",
                    kostraRecordInTest("0", "1"),
                    expectedErrorMessage = "Det er ikke fylt ut bidrag (0) fordelt på måneder eller sum stemmer ikke med sum bidrag (1) utbetalt i løpet av året.",
                ),
                ForAllRowItem(
                    "bidrag = X, sum bidrag per måned = 1",
                    kostraRecordInTest(" ", "1"),
                    expectedErrorMessage = "Det er ikke fylt ut bidrag (0) fordelt på måneder eller sum stemmer ikke med sum bidrag (1) utbetalt i løpet av året.",
                ),
            ),
            expectedSeverity = Severity.WARNING
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            bidragMaaned: String,
            bidrag: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                BIDRAG_COL_NAME to bidrag,
                BIDRAG_1_COL_NAME to bidragMaaned,
                BIDRAG_2_COL_NAME to " ",
                BIDRAG_3_COL_NAME to " ",
                BIDRAG_4_COL_NAME to " ",
                BIDRAG_5_COL_NAME to " ",
                BIDRAG_6_COL_NAME to " ",
                BIDRAG_7_COL_NAME to " ",
                BIDRAG_8_COL_NAME to " ",
                BIDRAG_9_COL_NAME to " ",
                BIDRAG_10_COL_NAME to " ",
                BIDRAG_11_COL_NAME to " ",
                BIDRAG_12_COL_NAME to " "
            )
        )
    }
}