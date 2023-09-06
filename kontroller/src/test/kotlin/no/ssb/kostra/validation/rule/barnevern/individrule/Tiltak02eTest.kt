package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02eTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Tiltak02e(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id,
            ForAllRowItem(
                "individ without tiltak",
                individInTest
            ),
            ForAllRowItem(
                "tiltak with startDato equal to individ startDato",
                individInTest.copy(
                    tiltak = mutableListOf(tiltakTypeInTest)
                )
            ),
            ForAllRowItem(
                "tiltak with startDato before individ startDato",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(startDato = dateInTest.minusDays(1))
                    )
                ),
                expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). StartDato " +
                        "(${dateInTest.minusDays(1)}) skal være lik eller etter " +
                        "individets startdato (${individInTest.startDato})"
            )
        )
    )
})
