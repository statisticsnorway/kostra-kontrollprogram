package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Lovhjemmel04Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Lovhjemmel04(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with tiltak with valid lovhjemmel",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest)
                    )
                ),

                ForAllRowItem(
                    "invalid kapittel",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                lovhjemmel = lovhjemmelTypeInTest.copy(kapittel = "0")
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Kapittel " +
                            "(0) eller paragraf (${lovhjemmelTypeInTest.paragraf}) " +
                            "er rapportert med den ugyldige koden 0"
                ),
                ForAllRowItem(
                    "invalid paragraf",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                lovhjemmel = lovhjemmelTypeInTest.copy(paragraf = "0")
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Kapittel " +
                            "(${lovhjemmelTypeInTest.kapittel}) eller paragraf (0) " +
                            "er rapportert med den ugyldige koden 0"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
