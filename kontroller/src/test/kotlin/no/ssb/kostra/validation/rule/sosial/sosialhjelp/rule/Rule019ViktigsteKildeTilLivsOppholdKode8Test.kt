package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule019ViktigsteKildeTilLivsOppholdKode8Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule019ViktigsteKildeTilLivsOppholdKode8(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, arbsitCode = 00",
                    kostraRecordInTest("0", "00"),
                ),
                ForAllRowItem(
                    "vkloCode = 8, arbsitCode = 10",
                    kostraRecordInTest("8", "10"),
                ),
                ForAllRowItem(
                    "vkloCode = 8, arbsitCode = 00",
                    kostraRecordInTest("8", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Kvalifiseringsstønad. " +
                            "Arbeidssituasjonen er '(00)', forventet én av '([" +
                            "10=Kvalifiseringsprogram" +
                            "])'. Feltet er obligatorisk å fylle ut.",
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vkloCode: String,
            arbsitCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VKLO_COL_NAME to vkloCode,
                ARBSIT_COL_NAME to arbsitCode
            )
        )
    }
}