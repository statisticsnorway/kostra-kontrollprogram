package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule014ViktigsteKildeTilLivsOppholdGyldigeVerdierTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule014ViktigsteKildeTilLivsOppholdGyldigeVerdier(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 1",
                    kostraRecordInTest("1"),
                ),
                ForAllRowItem(
                    "vkloCode = 0",
                    kostraRecordInTest("0"),
                    expectedErrorMessage = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med " +
                            "sosial-/NAV-kontoret skal oppgis. Fant '(0)', forventet én av '([1=Arbeidsinntekt, " +
                            "2=Kursstønad/lønn i arbeidsmarkedstiltak, 3=Trygd/pensjon, 4=Stipend/lån, 5=Sosialhjelp, " +
                            "6=Introduksjonsstøtte, 7=Ektefelle/samboers arbeidsinntekt, 8=Kvalifiseringsstønad, 9=Annen inntekt])'.",
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vkloCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VKLO_COL_NAME to vkloCode
            )
        )
    }
}
