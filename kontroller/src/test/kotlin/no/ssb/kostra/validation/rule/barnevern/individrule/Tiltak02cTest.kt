package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02cTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Tiltak02c(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id,
            ForAllRowItem(
                "individ without tiltak",
                individInTest
            ),
            ForAllRowItem(
                "individ with sluttDato, without tiltak",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1)
                )
            ),
            ForAllRowItem(
                "tiltak without sluttDato",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    tiltak = mutableListOf(tiltakTypeInTest)
                )
            ),
            ForAllRowItem(
                "tiltak with sluttDato equal to individ",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "tiltak with sluttDato after individ sluttDato",
                individInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(2)
                        )
                    )
                ),
                expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Sluttdato " +
                        "(${dateInTest.plusDays(2)}) er etter individets sluttdato " +
                        "(${dateInTest.plusDays(1)})"
            )
        )
    )
})
