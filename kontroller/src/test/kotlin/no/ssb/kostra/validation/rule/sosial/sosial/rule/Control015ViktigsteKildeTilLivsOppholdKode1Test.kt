package no.ssb.kostra.validation.rule.sosial.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Control015ViktigsteKildeTilLivsOppholdKode1Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Control015ViktigsteKildeTilLivsOppholdKode1(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, arbsitCode = 0",
                    kostraRecordInTest("0", "0")
                ),
                ForAllRowItem(
                    "vkloCode = 1, arbsitCode = 03",
                    kostraRecordInTest("1", "03")
                ),
                ForAllRowItem(
                    "vkloCode = 1, arbsitCode = 00",
                    kostraRecordInTest("1", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Arbeidsinntekt. " +
                            "Arbeidssituasjonen er '(00)', forventet én av '(["+
                            "03=Under utdanning, 05=Arbeidsmarkedstiltak (statlig), 06=Kommunalt tiltak" +
                            "])'. Feltet er obligatorisk å fylle ut."
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
        ) = SosialTestUtils.sosialKostraRecordInTest(
            mapOf(
                VKLO_COL_NAME to vkloCode,
                ARBSIT_COL_NAME to arbsitCode
            )
        )
    }
}