package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ02aTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ02a(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without endDate",
                    kostraIndividInTest,
                    false
                ),
                ForAllRowItem(
                    "individ with valid endDate",
                    kostraIndividInTest.copy(sluttDato = dateInTest.plusDays(1)),
                    false
                ),
                ForAllRowItem(
                    "individ with invalid startdato",
                    kostraIndividInTest.copy(sluttDato = dateInTest.minusDays(1)),
                    true
                ),
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Individets startdato ($dateInTest) er etter " +
                    "sluttdato (${dateInTest.minusDays(1)})",
            expectedContextId = kostraIndividInTest.id
        )
    )
})
