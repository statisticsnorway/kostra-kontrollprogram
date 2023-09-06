package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest

class Plan02cTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Plan02c(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = planTypeInTest.id,
            ForAllRowItem(
                "individ without sluttDato",
                individInTest
            ),
            ForAllRowItem(
                "individ with sluttDato, without plan",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1)
                )
            ),
            ForAllRowItem(
                "plan without sluttDato",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    plan = mutableListOf(planTypeInTest)
                )
            ),
            ForAllRowItem(
                "plan with sluttDato equal to individ",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    plan = mutableListOf(
                        planTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "plan with sluttDato after individ sluttDato",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    plan = mutableListOf(
                        planTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(2)
                        )
                    )
                ),
                expectedErrorMessage = "Plan (${planTypeInTest.id}). Planens sluttdato " +
                        "(${dateInTest.plusDays(2)}) er etter individets sluttdato " +
                        "(${dateInTest.plusDays(1)})"
            )
        )
    )
})
