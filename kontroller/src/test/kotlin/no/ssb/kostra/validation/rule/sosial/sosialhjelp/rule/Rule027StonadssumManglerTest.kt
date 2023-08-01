package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule027StonadssumManglerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule027StonadssumMangler(),
            forAllRows = listOf(
                ForAllRowItem(
                    "bidrag = blank, laan = blank",
                    kostraRecordInTest("  ", "  "),
                    expectedErrorMessage = "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag (  ) eller lån (  )) i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                ),
                ForAllRowItem(
                    "bidrag = XX, laan = XX",
                    kostraRecordInTest("XX", "XX"),
                    expectedErrorMessage = "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag (XX) eller lån (XX)) i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                ),
                ForAllRowItem(
                    "bidrag = 0, laan = 0",
                    kostraRecordInTest(" 0", " 0"),
                    expectedErrorMessage = "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag ( 0) eller lån ( 0)) i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                ),
                ForAllRowItem(
                    "bidrag = 1000, laan = 0",
                    kostraRecordInTest("1000", "0")
                ),
                ForAllRowItem(
                    "bidrag = 0, laan = 1000",
                    kostraRecordInTest("0", "1000")
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            bidrag: String,
            laan: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                BIDRAG_COL_NAME to bidrag,
                LAAN_COL_NAME to laan
            )
        )
    }
}