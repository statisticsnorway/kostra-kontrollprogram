package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.STMND_9_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule026StonadsmaanederTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule026Stonadsmaaneder(),
            forAllRows = listOf(
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
                ),
            ),
            expectedSeverity = Severity.ERROR
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