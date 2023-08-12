package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule021ViktigsteKildeTilLivsOppholdKode5Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule021ViktigsteKildeTilLivsOppholdKode5(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "vkloCode = 0, arbsitCode = 00",
                kostraRecordInTest("0", "00"),
            ),
            ForAllRowItem(
                "vkloCode = 5, arbsitCode = 02",
                kostraRecordInTest("5", "02"),
            ),
            ForAllRowItem(
                "vkloCode = 5, arbsitCode = 00",
                kostraRecordInTest("5", "00"),
                expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                        "sosial-/NAV-kontoret er Sosialhjelp. " +
                        "Arbeidssituasjonen er '(00)', forventet én av '([" +
                        "02=Arbeid, deltid, 04=Ikke arbeidssøker, 05=Arbeidsmarkedstiltak (statlig), " +
                        "06=Kommunalt tiltak, 07=Registrert arbeidsledig, 08=Arbeidsledig, men ikke registrert hos NAV" +
                        "])'. Feltet er obligatorisk å fylle ut.",
            )
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