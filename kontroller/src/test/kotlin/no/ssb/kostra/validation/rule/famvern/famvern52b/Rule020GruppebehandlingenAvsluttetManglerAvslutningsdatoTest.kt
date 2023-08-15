package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule020GruppebehandlingenAvsluttetManglerAvslutningsdatoTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule020GruppebehandlingenAvsluttetManglerAvslutningsdato(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "no match",
                kostraRecordInTest("X", "        "),
            ),
            ForAllRowItem(
                "all good",
                kostraRecordInTest("2", "01012023"),
            ),
            ForAllRowItem(
                "invalid value",
                kostraRecordInTest("2", "        "),
                expectedErrorMessage = "Det er krysset av for at gruppebehandlingen er avsluttet i " +
                "rapporteringsåret, men ikke fylt ut dato '        ' for avslutning av saken.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, avslutningsdato: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52bColumnNames.STATUS_ARETSSL_B_COL_NAME to status,
                    Familievern52bColumnNames.DATO_GRAVSLUTN_B_COL_NAME to avslutningsdato
                )
            )
        )
    }
}