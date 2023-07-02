package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding02aTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Melding02a(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest
                ),
                ForAllRowItem(
                    "melding without sluttDato",
                    individInTest.copy(melding = mutableListOf(meldingTypeInTest))
                ),
                ForAllRowItem(
                    "melding with sluttDato after startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
                        )
                    )
                ),

                ForAllRowItem(
                    "melding with sluttDato before startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
                        )
                    ),
                    expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Meldingens startdato " +
                            "(${meldingTypeInTest.startDato}) er etter meldingens sluttdato " +
                            "(${dateInTest.minusDays(1)})"
                ),
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = meldingTypeInTest.id
        )
    )
})
