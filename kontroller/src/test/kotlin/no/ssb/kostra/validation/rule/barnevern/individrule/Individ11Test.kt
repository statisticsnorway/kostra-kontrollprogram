package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ11Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Individ11(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with valid fodselsnummer",
                    individInTest
                ),

                ForAllRowItem(
                    "individ without fodselsnummer",
                    individInTest.copy(fodselsnummer = null),
                    expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
                ),
                ForAllRowItem(
                    "individ with empty fodselsnummer",
                    individInTest.copy(fodselsnummer = ""),
                    expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
                ),
                ForAllRowItem(
                    "individ with blank fodselsnummer",
                    individInTest.copy(fodselsnummer = " ".repeat(11)),
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