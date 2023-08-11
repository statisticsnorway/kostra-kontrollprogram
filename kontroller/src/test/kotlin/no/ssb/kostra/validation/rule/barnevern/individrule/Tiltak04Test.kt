package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.omsorgLovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.opphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak04Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Tiltak04(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "tiltak without sluttdato",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "tiltak with sluttdato, not omsorgstiltak",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(1),
                                lovhjemmel = lovhjemmelTypeInTest
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "tiltak with sluttdato, omsorgstiltak",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(1),
                                lovhjemmel = omsorgLovhjemmelTypeInTest,
                                opphevelse = opphevelseTypeInTest
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "tiltak with sluttdato, omsorgstiltak, opphevelse missing",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(1),
                                lovhjemmel = omsorgLovhjemmelTypeInTest,
                                opphevelse = null
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Omsorgstiltak med sluttdato " +
                            "(${dateInTest.plusDays(1)}) krever kode for opphevelse"
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
