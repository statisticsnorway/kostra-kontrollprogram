package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ11Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ11(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with valid fodselsnummer",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "individ without fodselsnummer",
                    individInTest.copy(fodselsnummer = null),
                    true
                ),
                ForAllRowItem(
                    "individ with empty fodselsnummer",
                    individInTest.copy(fodselsnummer = ""),
                    true
                ),
                ForAllRowItem(
                    "individ with blank fodselsnummer",
                    individInTest.copy(fodselsnummer = " ".repeat(11)),
                    true
                ),
                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    individInTest.copy(fodselsnummer = "12345612345"),
                    true
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer.",
            expectedContextId = individInTest.id
        )
    )
})
