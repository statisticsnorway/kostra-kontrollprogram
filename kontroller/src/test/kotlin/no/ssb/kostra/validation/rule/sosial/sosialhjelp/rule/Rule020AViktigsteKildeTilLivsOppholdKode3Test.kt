package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule020AViktigsteKildeTilLivsOppholdKode3Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule020AViktigsteKildeTilLivsOppholdKode3(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "vkloCode = 3, trygdesitCode = 00",
                kostraRecordInTest("3", "00"),
            ),
            ForAllRowItem(
                "vkloCode = 1, trygdesitCode = 12",
                kostraRecordInTest("1", "12"),
            ),
            ForAllRowItem(
                "vkloCode = 1, trygdesitCode = 00",
                kostraRecordInTest("1", "00"),
                expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                        "sosial-/NAV-kontoret er Arbeidsinntekt. " +
                        "Trygdesituasjonen er '(00)', forventet én av '([12=Har ingen trygd/pensjon])'. " +
                        "Feltet er obligatorisk å fylle ut.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vkloCode: String,
            trygdesitCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialhjelpColumnNames.VKLO_COL_NAME to vkloCode,
                SosialhjelpColumnNames.TRYGDESIT_COL_NAME to trygdesitCode
            )
        )
    }
}