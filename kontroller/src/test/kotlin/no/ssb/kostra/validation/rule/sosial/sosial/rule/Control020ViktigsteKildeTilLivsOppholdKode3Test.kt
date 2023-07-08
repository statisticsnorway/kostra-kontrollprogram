package no.ssb.kostra.validation.rule.sosial.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Control020ViktigsteKildeTilLivsOppholdKode3Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Control020ViktigsteKildeTilLivsOppholdKode3(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, trygdesitCode = 00",
                    kostraRecordInTest("0", "00")
                ),
                ForAllRowItem(
                    "vkloCode = 3, trygdesitCode = 10",
                    kostraRecordInTest("3", "10")
                ),
                ForAllRowItem(
                    "vkloCode = 3, trygdesitCode = 00",
                    kostraRecordInTest("3", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Trygd/pensjon. " +
                            "Arbeidssituasjonen er '(00)', forventet én av '([" +
                            "01=Sykepenger, 02=Dagpenger, 04=Uføretrygd, 05=Overgangsstønad, 06=Etterlattepensjon, " +
                            "07=Alderspensjon, 09=Supplerende stønad (kort botid), 10=Annen trygd, " +
                            "11=Arbeidsavklaringspenger" +
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
            trygdesitCode: String
        ) = SosialTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialColumnNames.VKLO_COL_NAME to vkloCode,
                SosialColumnNames.TRYGDESIT_COL_NAME to trygdesitCode
            )
        )
    }
}