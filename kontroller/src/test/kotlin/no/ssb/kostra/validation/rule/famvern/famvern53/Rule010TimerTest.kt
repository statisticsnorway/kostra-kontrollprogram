package no.ssb.kostra.validation.rule.famvern.famvern53

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames
import no.ssb.kostra.area.famvern.famvern53.Familievern53Constants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule010TimerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule010Timer(
                listOf(
                    Familievern53Constants.Rule010Mapping(
                        "Andre tiltak mot publikum",
                        Familievern53ColumnNames.TILTAK_PUBLIKUM_TILTAK_COL_NAME,
                        Familievern53ColumnNames.TILTAK_PUBLIKUM_TIMER_COL_NAME
                    ),
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid tiltak and timer",
                kostraRecordInTest("1", "1"),
            ),
            ForAllRowItem(
                "valid tiltak and invalid timer",
                kostraRecordInTest("1", " "),
                expectedErrorMessage = "Det er ikke fylt hvor mange timer ' ' kontoret har gjennomført når det " +
                        "gjelder 'Andre tiltak mot publikum, timer'. Sjekk om det er glemt å rapportere " +
                        "'Andre tiltak mot publikum, timer'.",
            ),
            ForAllRowItem(
                "invalid tiltak and invalid timer",
                kostraRecordInTest("X", "X"),
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(tiltak: String, timer: String) = listOf(
            Familievern53TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern53ColumnNames.TILTAK_PUBLIKUM_TILTAK_COL_NAME to tiltak,
                    Familievern53ColumnNames.TILTAK_PUBLIKUM_TIMER_COL_NAME to timer
                )
            )
        )
    }
}