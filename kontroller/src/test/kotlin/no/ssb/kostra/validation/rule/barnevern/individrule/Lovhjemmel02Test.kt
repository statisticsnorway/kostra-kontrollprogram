package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.opphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.omsorgLovhjemmelTypeInTest

class Lovhjemmel02Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Lovhjemmel02(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "individ with tiltak, erOmsorgstiltak = false",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest)
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ with tiltak, erOmsorgstiltak = true, sluttDato = null",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = null,
                                lovhjemmel = omsorgLovhjemmelTypeInTest
                            )
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ with tiltak, erOmsorgstiltak = true, sluttDato !=, opphevelse != null",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest,
                                lovhjemmel = omsorgLovhjemmelTypeInTest,
                                opphevelse = opphevelseTypeInTest
                            )
                        )
                    ),
                    false
                ),

                ForAllRowItem(
                    "individ with tiltak, erOmsorgstiltak = true, sluttDato !=, opphevelse = null",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest,
                                lovhjemmel = omsorgLovhjemmelTypeInTest,
                                opphevelse = null
                            )
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedErrorMessage = "Lovhjemmel Kontroll 2: Omsorgstiltak med sluttdato krever årsak til opphevelse",
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
