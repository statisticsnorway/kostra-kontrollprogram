package no.ssb.kostra.validation.rule.sosial.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Control016ViktigsteKildeTilLivsOppholdKode2Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Control016ViktigsteKildeTilLivsOppholdKode2(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, arbsitCode = 00",
                    kostraRecordInTest("0", "00")
                ),
                ForAllRowItem(
                    "vkloCode = 2, arbsitCode = 03",
                    kostraRecordInTest("2", "03")
                ),
                ForAllRowItem(
                    "vkloCode = 2, arbsitCode = 00",
                    kostraRecordInTest("2", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Kursstønad/lønn i arbeidsmarkedstiltak. " +
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
                SosialColumnNames.VKLO_COL_NAME to vkloCode,
                SosialColumnNames.ARBSIT_COL_NAME to arbsitCode
            )
        )
    }
}