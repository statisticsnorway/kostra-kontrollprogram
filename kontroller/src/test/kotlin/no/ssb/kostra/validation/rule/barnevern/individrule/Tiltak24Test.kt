package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL2021
import java.time.LocalDate

class Tiltak24Test : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleWithArgsTest(
            sut = Tiltak24(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = IndividRuleTestData.tiltakTypeInTest.id,
            ForAllRowItem(
                "individ without tiltak",
                IndividRuleTestData.individInTest
            ),
            ForAllRowItem(
                "tiltak with startdato before 01.01.2023, no errors",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2022, 12, 31),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest,
                            jmfrLovhjemmel = mutableListOf(IndividRuleTestData.lovhjemmelTypeInTest)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "tiltak with startdato before 01.01.2023, wrong lovhjemmel.lov",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2022, 12, 31),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest.copy(
                                lov = "FAIL"
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Tiltak (${IndividRuleTestData.tiltakTypeInTest.id}). " +
                        "Tiltak opprettet før 01.01.2023 krever lov = '$BVL1992'"
            ),
            ForAllRowItem(
                "tiltak with startdato before 01.01.2023, wrong jmfrlovhjemmel.lov",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2022, 12, 31),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest,
                            jmfrLovhjemmel = mutableListOf(
                                IndividRuleTestData.lovhjemmelTypeInTest.copy(
                                    lov = "FAIL"
                                )
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Tiltak (${IndividRuleTestData.tiltakTypeInTest.id}). " +
                        "Tiltak opprettet før 01.01.2023 krever lov = '$BVL1992'"
            ),
            ForAllRowItem(
                "tiltak with startdato after 01.01.2023, no errors, lov = '$BVL1992'",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2023, 1, 1),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest,
                            jmfrLovhjemmel = mutableListOf(IndividRuleTestData.lovhjemmelTypeInTest)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "tiltak with startdato after 01.01.2023, no errors, lov = '$BVL2021'",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2023, 1, 1),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest.copy(lov = BVL2021),
                            jmfrLovhjemmel = mutableListOf(IndividRuleTestData.lovhjemmelTypeInTest)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "tiltak with startdato after 01.01.2023, wrong lovhjemmel.lov",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2023, 1, 1),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest.copy(
                                lov = "FAIL"
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Tiltak (${IndividRuleTestData.tiltakTypeInTest.id}). " +
                        "Tiltak opprettet 01.01.2023 eller etter, krever lov = '$BVL1992' eller '$BVL2021'"
            ),
            ForAllRowItem(
                "tiltak with startdato after 01.01.2023, wrong jmfrlovhjemmel.lov",
                IndividRuleTestData.individInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.tiltakTypeInTest.copy(
                            startDato = LocalDate.of(2023, 1, 1),
                            lovhjemmel = IndividRuleTestData.lovhjemmelTypeInTest,
                            jmfrLovhjemmel = mutableListOf(
                                IndividRuleTestData.lovhjemmelTypeInTest.copy(
                                    lov = "FAIL"
                                )
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Tiltak (${IndividRuleTestData.tiltakTypeInTest.id}). " +
                        "Tiltak opprettet 01.01.2023 eller etter, krever lov = '$BVL1992' eller '$BVL2021'"
            ),
        )
    )
})
