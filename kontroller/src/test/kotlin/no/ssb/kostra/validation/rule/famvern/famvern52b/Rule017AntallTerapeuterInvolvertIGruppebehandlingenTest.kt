package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule017AntallTerapeuterInvolvertIGruppebehandlingenTest :
    BehaviorSpec({
        include(
            KostraTestFactory.validationRuleNoContextTest(
                sut = Rule017AntallTerapeuterInvolvertIGruppebehandlingen(),
                expectedSeverity = Severity.WARNING,
                ForAllRowItem(
                    "valid count",
                    kostraRecordInTest("1"),
                ),
                ForAllRowItem(
                    "missing count",
                    kostraRecordInTest(" "),
                    expectedErrorMessage =
                        "Det er ikke oppgitt hvor mange hovedterapeuter eller " +
                            "andre ansatte som har deltatt i gruppen.",
                ),
                ForAllRowItem(
                    "invalid count, illegal characters",
                    kostraRecordInTest("X"),
                    expectedErrorMessage =
                        "Det er ikke oppgitt hvor mange hovedterapeuter eller " +
                            "andre ansatte som har deltatt i gruppen.",
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordInTest(count: String) =
            listOf(
                Familievern52bTestUtils.familievernRecordInTest(
                    mapOf(Familievern52bColumnNames.ANTTER_GRUPPEB_B_COL_NAME to count),
                ),
            )
    }
}
