package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ02aTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Individ02a(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without endDate",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with valid endDate",
                    individInTest.copy(sluttDato = dateInTest.plusDays(1))
                ),

                ForAllRowItem(
                    "individ with invalid startdato",
                    individInTest.copy(sluttDato = dateInTest.minusDays(1)),
                    expectedErrorMessage = "Individets startdato ($dateInTest) er etter " +
                            "sluttdato (${dateInTest.minusDays(1)})"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id
        )
    )
})
