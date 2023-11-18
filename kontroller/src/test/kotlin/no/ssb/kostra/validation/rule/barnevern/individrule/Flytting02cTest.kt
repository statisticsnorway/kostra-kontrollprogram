package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Flytting02cTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleWithArgsTest(
            sut = Flytting02c(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = IndividRuleTestData.flyttingTypeInTest.id,
            ForAllRowItem(
                "individ without flytting",
                IndividRuleTestData.individInTest
            ),
            ForAllRowItem(
                "individ with sluttDato, without flytting",
                IndividRuleTestData.individInTest.copy(
                    sluttDato = IndividRuleTestData.dateInTest.plusDays(1)
                )
            ),
            ForAllRowItem(
                "flytting without sluttDato",
                IndividRuleTestData.individInTest.copy(
                    sluttDato = IndividRuleTestData.dateInTest.plusDays(1),
                    flytting = mutableListOf(IndividRuleTestData.flyttingTypeInTest)
                )
            ),
            ForAllRowItem(
                "flytting with sluttDato equal to individ",
                IndividRuleTestData.individInTest.copy(
                    sluttDato = IndividRuleTestData.dateInTest.plusDays(1),
                    flytting = mutableListOf(
                        IndividRuleTestData.flyttingTypeInTest.copy(
                            sluttDato = IndividRuleTestData.dateInTest.plusDays(1)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "flytting with sluttDato after individ sluttDato",
                IndividRuleTestData.individInTest.copy(
                    sluttDato = IndividRuleTestData.dateInTest.plusDays(1),
                    flytting = mutableListOf(
                        IndividRuleTestData.flyttingTypeInTest.copy(
                            sluttDato = IndividRuleTestData.dateInTest.plusDays(2)
                        )
                    )
                ),
                expectedErrorMessage = "Flytting (${IndividRuleTestData.flyttingTypeInTest.id}). Sluttdato " +
                        "(${IndividRuleTestData.dateInTest.plusDays(2)}) er etter individets sluttdato " +
                        "(${IndividRuleTestData.dateInTest.plusDays(1)})"
            )
        )
    )
})
