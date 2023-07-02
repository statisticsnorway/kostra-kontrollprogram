package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.omsorgLovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.opphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Lovhjemmel02Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Lovhjemmel02(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with tiltak, erOmsorgstiltak = false",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest)
                    )
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
                    )
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
                    )
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
                    expectedErrorMessage = "Lovhjemmel Kontroll 2: Omsorgstiltak med sluttdato " +
                            "krever årsak til opphevelse"
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
