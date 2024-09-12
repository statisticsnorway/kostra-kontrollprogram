package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule020BViktigsteKildeTilLivsOppholdKode3Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule020BViktigsteKildeTilLivsOppholdKode3(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "vkloCode = 0, trygdesitCode = 00",
                kostraRecordInTest("0", "00"),
            ),
            ForAllRowItem(
                "vkloCode = 3, trygdesitCode = 10",
                kostraRecordInTest("3", "10"),
            ),
            ForAllRowItem(
                "vkloCode = 3, trygdesitCode = 00",
                kostraRecordInTest("3", "00"),
                expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                        "sosial-/NAV-kontoret er Trygd/pensjon. " +
                        "Trygdesituasjonen er '(00)', forventet én av '([" +
                        "01=Sykepenger, 02=Dagpenger, 04=Uføretrygd, 05=Overgangsstønad, 06=Etterlattepensjon, " +
                        "07=Alderspensjon, 09=Supplerende stønad (kort botid), 10=Annen trygd, " +
                        "11=Arbeidsavklaringspenger, 13=Barnetrygd" +
                        "])'. Feltet er obligatorisk å fylle ut.",
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
                VKLO_COL_NAME to vkloCode,
                TRYGDESIT_COL_NAME to trygdesitCode
            )
        )
    }
}