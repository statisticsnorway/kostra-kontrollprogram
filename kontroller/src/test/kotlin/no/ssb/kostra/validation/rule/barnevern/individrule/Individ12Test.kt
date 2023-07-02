package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ12Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ12(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with valid fodselsnummer",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with 99999 fodselsnummer",
                    individInTest.copy(fodselsnummer = "02011099999")
                ),

                ForAllRowItem(
                    "individ without fodselsnummer",
                    individInTest.copy(fodselsnummer = null),
                    expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
                ),
                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    individInTest.copy(fodselsnummer = "12345612345"),
                    expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = individInTest.id
        )
    )
})
