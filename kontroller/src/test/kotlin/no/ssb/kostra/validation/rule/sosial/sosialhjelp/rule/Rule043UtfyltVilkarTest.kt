package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARANNET_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARARBEID_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARDIGPLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARJOBBLOG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARJOBBTILB_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARKURS_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARLIVSH_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARNORSKSAMISKOPPL_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKAROKRETT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSAMT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARUTD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule043UtfyltVilkarTest :
    BehaviorSpec({
        include(
            validationRuleNoContextTest(
                sut = Rule043UtfyltVilkar(),
                expectedSeverity = Severity.ERROR,
                ForAllRowItem(
                    "vilkar = 2, arbeid = XX",
                    kostraRecordInTest("2", "XX"),
                ),
                ForAllRowItem(
                    "vilkar = 1, arbeid = 16",
                    kostraRecordInTest("1", "16"),
                ),
                ForAllRowItem(
                    "vilkar = 1, arbeid = XX",
                    kostraRecordInTest("1", "XX"),
                    expectedErrorMessage =
                        "Feltet for " +
                            "'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', " +
                            "så skal det oppgis hvilke vilkår som stilles til mottakeren. Feltet er obligatorisk å fylle ut.",
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordInTest(
            vilkar: String,
            arbeid: String,
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VILKARSOSLOV_COL_NAME to vilkar,
                VILKARARBEID_COL_NAME to arbeid,
                VILKARKURS_COL_NAME to " ",
                VILKARUTD_COL_NAME to " ",
                VILKARJOBBLOG_COL_NAME to " ",
                VILKARJOBBTILB_COL_NAME to " ",
                VILKARSAMT_COL_NAME to " ",
                VILKAROKRETT_COL_NAME to " ",
                VILKARLIVSH_COL_NAME to " ",
                VILKARHELSE_COL_NAME to " ",
                VILKARANNET_COL_NAME to " ",
                VILKARDIGPLAN_COL_NAME to " ",
                VILKARNORSKSAMISKOPPL_COL_NAME to " ",
            ),
        )
    }
}
