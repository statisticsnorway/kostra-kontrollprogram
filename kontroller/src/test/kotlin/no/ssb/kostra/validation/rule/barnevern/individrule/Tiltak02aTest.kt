package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02aTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Tiltak02a(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "tiltak without sluttDato",
                    individInTest.copy(tiltak = mutableListOf(tiltakTypeInTest))
                ),
                ForAllRowItem(
                    "tiltak with sluttDato after startDato",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
                        )
                    )
                ),

                ForAllRowItem(
                    "tiltak with sluttDato before startDato",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Startdato " +
                            "(${tiltakTypeInTest.startDato}) for tiltaket er " +
                            "etter sluttdato (${dateInTest.minusDays(1)}) for tiltaket"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
