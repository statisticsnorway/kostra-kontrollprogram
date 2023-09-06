package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest

class Plan02bTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Plan02b(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = planTypeInTest.id,
            ForAllRowItem(
                "individ without plan",
                individInTest
            ),
            ForAllRowItem(
                "plan without sluttDato",
                individInTest.copy(plan = mutableListOf(planTypeInTest))
            ),
            ForAllRowItem(
                "plan with sluttDato in reporting year",
                individInTest.copy(
                    plan = mutableListOf(
                        planTypeInTest.copy(sluttDato = dateInTest.minusYears(1))
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
                expectedErrorMessage = "Plan (${planTypeInTest.id}). Planens sluttdato " +
                        "(${dateInTest.minusDays(1)}) er ikke i rapporteringsåret " +
                        "(${argumentsInTest.aargang})"
            )
        )
    )
})
