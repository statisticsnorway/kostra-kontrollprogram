package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02dTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Tiltak02d(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with avslutta3112 = '2'",
                    individInTest
                ),
                ForAllRowItem(
                    "avslutta3112 = '1', no tiltak", individInTest.copy(
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE
                    )
                ),
                ForAllRowItem(
                    "avslutta3112 = '1', tiltak with sluttDato",
                    individInTest.copy(
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                        sluttDato = dateInTest.minusYears(1).plusDays(1),
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest.minusYears(1).plusDays(1)
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "avslutta3112 = '1', tiltak without sluttDato",
                    individInTest.copy(
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                        sluttDato = dateInTest.minusYears(1).plusDays(1),
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = null
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Individet er avsluttet hos barnevernet " +
                            "og dets tiltak skal dermed være avsluttet. Sluttdato er uoppgitt"
                ),
                ForAllRowItem(
                    "avslutta3112 = '1', tiltak with valid sluttDato, context without sluttDato",
                    individInTest.copy(
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                        sluttDato = null,
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Individet er avsluttet hos barnevernet " +
                            "og dets tiltak skal dermed være avsluttet. Sluttdato er $dateInTest"
                ),
                ForAllRowItem(
                    "avslutta3112 = '1', tiltak with sluttDato after reporting year",
                    individInTest.copy(
                        avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                        sluttDato = dateInTest.minusYears(1).plusDays(1),
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                sluttDato = dateInTest
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Individet er avsluttet hos barnevernet " +
                            "og dets tiltak skal dermed være avsluttet. Sluttdato er $dateInTest"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
