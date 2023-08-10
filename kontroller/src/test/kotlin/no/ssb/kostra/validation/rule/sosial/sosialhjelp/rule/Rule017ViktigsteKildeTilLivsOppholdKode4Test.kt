package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule017ViktigsteKildeTilLivsOppholdKode4Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule017ViktigsteKildeTilLivsOppholdKode4(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, arbsitCode = 00",
                    kostraRecordInTest("0", "00"),
                ),
                ForAllRowItem(
                    "vkloCode = 4, arbsitCode = 03",
                    kostraRecordInTest("4", "03"),
                ),
                ForAllRowItem(
                    "vkloCode = 4, arbsitCode = 00",
                    kostraRecordInTest("4", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Stipend/lån. " +
                            "Arbeidssituasjonen er '(00)', forventet én av '([" +
                            "03=Under utdanning" +
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