package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem

class Avgiver06Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Avgiver06(),
            forAllRows = listOf(
                ForAllRowItem(
                    "avgiver with kommunenavn",
                    kostraAvgiverTypeInTest
                ),

                ForAllRowItem(
                    "avgiver with empty kommunenavn",
                    kostraAvgiverTypeInTest.copy(kommunenavn = ""),
                    expectedErrorMessage = "Filen mangler kommunenavn."
                ),
                ForAllRowItem(
                    "avgiver with blank kommunenavn",
                    kostraAvgiverTypeInTest.copy(kommunenavn = "   "),
                    expectedErrorMessage = "Filen mangler kommunenavn."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
})
