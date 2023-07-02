package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_FALSE
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ02dTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Individ02d(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without sluttDato",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with sluttDato, avslutta3112 = 2",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        avslutta3112 = KOSTRA_IS_CLOSED_FALSE
                    )
                ),
                ForAllRowItem(
                    "individ with sluttDato, avslutta3112 = 1",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE
                    )
                ),

                ForAllRowItem(
                    "individ with without sluttDato, avslutta3112 = 1 ",
                    individInTest.copy(avslutta3112 = KOSTRA_IS_CLOSED_TRUE),
                    expectedErrorMessage = "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. " +
                            "Sluttdato er ${null}. Kode for avsluttet er '$KOSTRA_IS_CLOSED_TRUE'."
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id
        )
    )
})
