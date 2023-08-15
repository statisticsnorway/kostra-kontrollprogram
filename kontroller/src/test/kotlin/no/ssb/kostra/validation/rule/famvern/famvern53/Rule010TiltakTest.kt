package no.ssb.kostra.validation.rule.famvern.famvern53

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames
import no.ssb.kostra.area.famvern.famvern53.Familievern53Constants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule010TiltakTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule010Tiltak(
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
                "valid tiltak",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid tiltak",
                kostraRecordInTest("0"),
                expectedErrorMessage = "Det er ikke fylt hvor mange tiltak (0) kontoret har gjennomført når det " +
                        "gjelder 'Andre tiltak mot publikum, tiltak'. Sjekk om det er glemt å rapportere " +
                        "'Andre tiltak mot publikum'.",
            ),
            ForAllRowItem(
                "invalid tiltak, illegal character",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt hvor mange tiltak (X) kontoret har gjennomført når det " +
                        "gjelder 'Andre tiltak mot publikum, tiltak'. Sjekk om det er glemt å rapportere " +
                        "'Andre tiltak mot publikum'.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(tiltak: String) = listOf(
            Familievern53TestUtils.familievernRecordInTest(
                mapOf(Familievern53ColumnNames.TILTAK_PUBLIKUM_TILTAK_COL_NAME to tiltak)
            )
        )
    }
}