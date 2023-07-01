package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity

class Avgiver04Test : BehaviorSpec({
    include(
        AvgiverTestFactory.avgiverTest(
            sut = Avgiver04(),
            forAllRows = listOf(
                Triple(
                    "avgiver with valid kommunenummer",
                    kostraAvgiverTypeInTest,
                    false
                ),
                Triple(
                    "avgiver with wrong kommunenummer",
                    kostraAvgiverTypeInTest.copy(kommunenummer = "4321"),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Filen inneholder feil kommunenummer. Forskjellig kommunenummer " +
                    "i skjema og filuttrekk. 4321 : ${kostraAvgiverTypeInTest.kommunenummer}"
        )
    )
})
