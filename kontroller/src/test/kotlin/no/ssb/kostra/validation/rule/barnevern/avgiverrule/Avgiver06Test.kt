package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem

class Avgiver06Test : BehaviorSpec({
    include(
        validationRuleTest(
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
