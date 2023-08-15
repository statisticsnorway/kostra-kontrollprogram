package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule035SakenAvsluttetManglerAvslutningsdatoTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule035SakenAvsluttetManglerAvslutningsdato(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "no match",
                kostraRecordInTest("X", "        "),
            ),
            ForAllRowItem(
                "all good",
                kostraRecordInTest("1", "01012023"),
            ),
            ForAllRowItem(
                "invalid value",
                kostraRecordInTest("1", "        "),
                expectedErrorMessage = "Det er krysset av for at saken er avsluttet i rapporteringsåret, " +
                        "men ikke fylt ut dato '        ' for avslutning av saken.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, avslutningsdato: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME to status,
                    Familievern52aColumnNames.DATO_AVSL_A_COL_NAME to avslutningsdato
                )
            )
        )
    }
}