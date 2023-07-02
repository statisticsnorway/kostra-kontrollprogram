package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.opphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak08Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Tiltak08(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with opphevelse that does not require presisering",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest.copy(opphevelse = opphevelseTypeInTest))
                    )
                ),
                ForAllRowItem(
                    "individ with opphevelse with presisering",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                opphevelse = opphevelseTypeInTest.copy(kode = "4")
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "individ with opphevelse without presisering",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                opphevelse = opphevelseTypeInTest.copy(
                                    kode = "4",
                                    presisering = null
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Tiltaksopphevelse (4) mangler presisering"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
