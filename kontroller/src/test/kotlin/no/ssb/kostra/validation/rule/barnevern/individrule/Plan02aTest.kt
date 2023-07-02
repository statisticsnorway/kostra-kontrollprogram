package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest

class Plan02aTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Plan02a(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without plan",
                    individInTest
                ),
                ForAllRowItem(
                    "plan without sluttDato",
                    individInTest.copy(plan = mutableListOf(planTypeInTest))
                ),
                ForAllRowItem(
                    "plan with sluttDato after startDato",
                    individInTest.copy(
                        plan = mutableListOf(
                            planTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
                        )
                    )
                ),

                ForAllRowItem(
                    "plan with sluttDato before startDato",
                    individInTest.copy(
                        plan = mutableListOf(
                            planTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
                        )
                    ),
                    expectedErrorMessage = "Plan (${planTypeInTest.id}). Planens startdato (${planTypeInTest.startDato}) " +
                            "er etter planens sluttdato (${dateInTest.minusDays(1)})"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = planTypeInTest.id
        )
    )
})
