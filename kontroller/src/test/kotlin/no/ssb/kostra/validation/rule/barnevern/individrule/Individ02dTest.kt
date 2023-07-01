package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_FALSE
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ02dTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ02d(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without sluttDato",
                    kostraIndividInTest,
                    false
                ),
                ForAllRowItem(
                    "individ with sluttDato, avslutta3112 = 2",
                    kostraIndividInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        avslutta3112 = KOSTRA_IS_CLOSED_FALSE
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ with sluttDato, avslutta3112 = 1",
                    kostraIndividInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ with without sluttDato, avslutta3112 = 1 ",
                    kostraIndividInTest.copy(avslutta3112 = KOSTRA_IS_CLOSED_TRUE),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. " +
                    "Sluttdato er ${null}. Kode for avsluttet er '$KOSTRA_IS_CLOSED_TRUE'.",
            expectedContextId = kostraIndividInTest.id
        )
    )
})
