package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity

class Avgiver06Test : BehaviorSpec({
    include(
        AvgiverTestFactory.avgiverTest(
            sut = Avgiver06(),
            forAllRows = listOf(
                Triple(
                    "avgiver with kommunenavn",
                    kostraAvgiverTypeInTest,
                    false
                ),
                Triple(
                    "avgiver with empty kommunenavn",
                    kostraAvgiverTypeInTest.copy(kommunenavn = ""),
                    true
                ),
                Triple(
                    "avgiver with blank kommunenavn",
                    kostraAvgiverTypeInTest.copy(kommunenavn = "   "),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Filen mangler kommunenavn."
        )
    )
})
