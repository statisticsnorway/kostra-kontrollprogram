package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ02bTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ02b(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without sluttDato",
                    kostraIndividInTest,
                    false
                ),
                ForAllRowItem(
                    "individ with valid sluttDato",
                    kostraIndividInTest.copy(sluttDato = dateInTest.plusDays(1)),
                    false
                ),
                ForAllRowItem(
                    "individ with invalid sluttDato",
                    kostraIndividInTest.copy(sluttDato = dateInTest.minusYears(2)),
                    true
                ),
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Individets sluttdato (${dateInTest.minusYears(2)}) er " +
                    "før forrige telletidspunkt",
            expectedContextId = kostraIndividInTest.id
        )
    )
})
