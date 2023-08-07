package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule009SivilstandTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule009Sivilstand(),
            forAllRows = listOf(
                *(1..5).map {
                    ForAllRowItem(
                        "record with sivilstand = $it",
                        kostraRecordInTest("$it"),
                    )
                }.toTypedArray(),
                ForAllRowItem(
                    "record with empty sivilstand",
                    kostraRecordInTest(""),
                    expectedErrorMessage = "Korrigér sivilstand. Fant '', " +
                            "forventet én av [1=Ugift, 2=Gift, 3=Samboer, 4=Skilt/separert, 5=Enke/enkemann]. " +
                            "Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er ikke " +
                            "fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut.",
                ),
                ForAllRowItem(
                    "record with invalid sivilstand",
                    kostraRecordInTest("42"),
                    expectedErrorMessage = "Korrigér sivilstand. Fant '42', " +
                            "forventet én av [1=Ugift, 2=Gift, 3=Samboer, 4=Skilt/separert, 5=Enke/enkemann]. " +
                            "Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er ikke " +
                            "fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut.",
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(maritalStatus: String) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    EKTSTAT_COL_NAME to maritalStatus
                )
            )
        )
    }
}