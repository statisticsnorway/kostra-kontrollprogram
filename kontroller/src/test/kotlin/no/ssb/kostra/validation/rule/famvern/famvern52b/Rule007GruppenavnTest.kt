package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bFieldDefinitions
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule007GruppenavnTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule007Gruppenavn(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid text",
                kostraRecordInTest("My Name"),
            ),
            ForAllRowItem(
                "missing text",
                kostraRecordInTest(
                    " ".repeat(
                        with(
                            Familievern52bFieldDefinitions.fieldDefinitions.byColumnName(
                                Familievern52bColumnNames.GRUPPE_NAVN_B_COL_NAME
                            )
                        ) { to + 1 - from })
                ),
                expectedErrorMessage = "Det er ikke oppgitt navn på gruppen. Tekstfeltet skal ha maksimalt " +
                        "30 posisjoner.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(text: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.GRUPPE_NAVN_B_COL_NAME to text)
            )
        )
    }
}