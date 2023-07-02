package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest

class Plan02eTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Plan02e(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without plan",
                    individInTest
                ),
                ForAllRowItem(
                    "plan with startDato equal to individ startDato",
                    individInTest.copy(
                        plan = mutableListOf(planTypeInTest)
                    )
                ),

                ForAllRowItem(
                    "plan with startDato before individ startDato",
                    individInTest.copy(
                        plan = mutableListOf(
                            planTypeInTest.copy(startDato = dateInTest.minusDays(1))
                        )
                    ),
                    expectedErrorMessage = "Plan (${planTypeInTest.id}). StartDato " +
                            "(${dateInTest.minusDays(1)}) skal være lik eller etter individets " +
                            "startdato (${individInTest.startDato})"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = planTypeInTest.id
        )
    )
})
