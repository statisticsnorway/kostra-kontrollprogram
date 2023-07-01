package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding02eTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Melding02e(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding", individInTest,
                    false
                ),
                ForAllRowItem(
                    "melding with startDato equal to individ startDato",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    ),
                    false
                ),

                ForAllRowItem(
                    "melding with startDato before individ startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(startDato = dateInTest.minusDays(1))
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Startdato " +
                    "(${dateInTest.minusDays(1)}) skal være lik eller etter individets startdato " +
                    "(${individInTest.startDato})",
            expectedContextId = meldingTypeInTest.id
        )
    )
})
