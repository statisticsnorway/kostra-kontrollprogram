package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ02aTest : BehaviorSpec({
    include(
        BarnevernTestFactory.barnevernValidationRuleTest(
            sut = Individ02a(),
            forAllRows = listOf(
                Triple(
                    "individ without endDate",
                    kostraIndividInTest,
                    false
                ),
                Triple(
                    "individ with valid endDate",
                    kostraIndividInTest.copy(sluttDato = dateInTest.plusDays(1)),
                    false
                ),
                Triple(
                    "individ with invalid startdato",
                    kostraIndividInTest.copy(sluttDato = dateInTest.minusDays(1)),
                    true
                ),
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Individets startdato ($dateInTest) er etter " +
                    "sluttdato (${dateInTest.minusDays(1)})"
        )
    )
})
