package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02aTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Tiltak02a(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id,
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
        )
    )
})
