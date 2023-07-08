package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Control018ViktigsteKildeTilLivsOppholdKode6Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Control018ViktigsteKildeTilLivsOppholdKode6(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, arbsitCode = 00",
                    kostraRecordInTest("0", "00")
                ),
                ForAllRowItem(
                    "vkloCode = 6, arbsitCode = 09",
                    kostraRecordInTest("6", "09")
                ),
                ForAllRowItem(
                    "vkloCode = 6, arbsitCode = 00",
                    kostraRecordInTest("6", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Introduksjonsstøtte. " +
                            "Arbeidssituasjonen er '(00)', forventet én av '([" +
                            "09=Introduksjonsordning" +
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