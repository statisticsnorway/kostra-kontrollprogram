package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ11Test : BehaviorSpec({
    include(
        BarnevernTestFactory.barnevernValidationRuleTest(
            sut = Individ11(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with normal fodselsnummer",
                    kostraIndividInTest,
                    false
                ),
                ForAllRowItem(
                    "individ without fodselsnummer",
                    kostraIndividInTest.copy(fodselsnummer = null),
                    true
                ),
                ForAllRowItem(
                    "individ with empty fodselsnummer",
                    kostraIndividInTest.copy(fodselsnummer = ""),
                    true
                ),
                ForAllRowItem(
                    "individ with blank fodselsnummer",
                    kostraIndividInTest.copy(fodselsnummer = " ".repeat(11)),
                    true
                ),
                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    kostraIndividInTest.copy(fodselsnummer = "12345612345"),
                    true
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer.",
            expectedContextId = kostraIndividInTest.id
        )
    )
})
