package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARANNET_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARARBEID_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARDIGPLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARJOBBLOG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARJOBBTILB_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARKURS_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARLIVSH_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKAROKRETT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSAMT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARUTD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule043UtfyltVilkarTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule043UtfyltVilkar(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vilkar = 2, arbeid = XX",
                    kostraRecordInTest("2", "XX")
                ),
                ForAllRowItem(
                    "vilkar = 1, arbeid = 16",
                    kostraRecordInTest("1", "16")
                ),
                ForAllRowItem(
                    "vilkar = 1, arbeid = XX",
                    kostraRecordInTest("1", "XX"),
                    expectedErrorMessage = "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', så skal det oppgis hvilke vilkår som stilles til mottakeren. Feltet er obligatorisk å fylle ut."
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vilkar: String,
            arbeid: String
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
            )
        )
    }
}