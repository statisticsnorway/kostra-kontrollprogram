package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding02eTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Melding02e(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest
                ),
                ForAllRowItem(
                    "melding with startDato equal to individ startDato",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),

                ForAllRowItem(
                    "melding with startDato before individ startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(startDato = dateInTest.minusDays(1))
                        )
                    ),
                    expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Startdato " +
                            "(${dateInTest.minusDays(1)}) skal være lik eller etter individets startdato " +
                            "(${individInTest.startDato})"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = meldingTypeInTest.id
        )
    )
})
