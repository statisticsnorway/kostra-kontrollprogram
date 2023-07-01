package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity

class Avgiver03Test : BehaviorSpec({
    include(
        AvgiverTestFactory.avgiverTest(
            sut = Avgiver03(),
            forAllRows = listOf(
                Triple(
                    "avgiver with organisasjonsnummer",
                    kostraAvgiverTypeInTest,
                    false
                ),
                Triple(
                    "avgiver with empty organisasjonsnummer",
                    kostraAvgiverTypeInTest.copy(organisasjonsnummer = ""),
                    true
                ),
                Triple(
                    "avgiver with blank organisasjonsnummer",
                    kostraAvgiverTypeInTest.copy(organisasjonsnummer = "  "),
                    true
                ),
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er"
        )
    )
})
