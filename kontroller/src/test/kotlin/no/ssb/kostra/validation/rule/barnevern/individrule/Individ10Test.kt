package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ10Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ10(),
            forAllRows = listOf(
                ForAllRowItem(
                    description = "individ without bydelsnavn",
                    individInTest.copy(bydelsnavn = null),
                    expectError = false,
                    arguments = argumentsInTest.copy(region = "123400")
                ),
                ForAllRowItem(
                    description = "individ with bydelsnavn, Oslo",
                    context = individInTest,
                    expectError = false,
                    arguments = argumentsInTest.copy(region = "${OSLO_MUNICIPALITY_ID}14")
                ),
                ForAllRowItem(
                    description = "individ without bydelsnavn, Oslo",
                    context = individInTest.copy(bydelsnavn = null),
                    expectError = true,
                    arguments = argumentsInTest.copy(region = "${OSLO_MUNICIPALITY_ID}14")
                ),
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Filen mangler bydelsnavn.",
            expectedContextId = individInTest.id
        )
    )
})
