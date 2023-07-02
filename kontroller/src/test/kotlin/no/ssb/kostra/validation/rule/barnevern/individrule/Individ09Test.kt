package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ09Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Individ09(),
            forAllRows = listOf(
                ForAllRowItem(
                    description = "individ without bydelsnummer",
                    context = individInTest.copy(bydelsnummer = null),
                    arguments = argumentsInTest.copy(region = "123400")
                ),
                ForAllRowItem(
                    description = "individ with bydelsnummer, Oslo",
                    context = individInTest,
                    arguments = argumentsInTest.copy(region = "${OSLO_MUNICIPALITY_ID}14")
                ),

                ForAllRowItem(
                    description = "individ without bydelsnummer, Oslo",
                    context = individInTest.copy(bydelsnummer = null),
                    expectedErrorMessage = "Filen mangler bydelsnummer.",
                    arguments = argumentsInTest.copy(region = "${OSLO_MUNICIPALITY_ID}14")
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id
        )
    )
})
