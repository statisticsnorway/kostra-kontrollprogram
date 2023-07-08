package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Control022TilknytningTilTrygdesystemetOgAlderTest: BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Control021ViktigsteKildeTilLivsOppholdKode5(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, arbsitCode = 00",
                    kostraRecordInTest("0", "00")
                ),
                ForAllRowItem(
                    "vkloCode = 5, arbsitCode = 02",
                    kostraRecordInTest("5", "02")
                ),
                ForAllRowItem(
                    "vkloCode = 5, arbsitCode = 00",
                    kostraRecordInTest("5", "00"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret er Sosialhjelp. " +
                            "Arbeidssituasjonen er '(00)', forventet én av '([" +
                            "02=Arbeid, deltid, 04=Ikke arbeidssøker, 05=Arbeidsmarkedstiltak (statlig), " +
                            "06=Kommunalt tiltak, 07=Registrert arbeidsledig, 08=Arbeidsledig, men ikke registrert hos NAV" +
                            "])'. Feltet er obligatorisk å fylle ut."
                )
            ),
            expectedSeverity = Severity.WARNING
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vkloCode: String,
            arbsitCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialColumnNames.VKLO_COL_NAME to vkloCode,
                SosialColumnNames.ARBSIT_COL_NAME to arbsitCode
            )
        )
    }
}