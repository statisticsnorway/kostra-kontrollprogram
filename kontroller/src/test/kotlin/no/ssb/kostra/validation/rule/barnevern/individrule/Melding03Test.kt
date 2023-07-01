package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding03Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Melding03(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "melding without sluttDato",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with sluttDato less than 8 days from startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(4))
                        )
                    ),
                    false
                ),

                ForAllRowItem(
                    "melding with sluttDato more than 8 days from startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(14))
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Fristoverskridelse på behandlingstid " +
                    "for melding, (${meldingTypeInTest.startDato} -> ${dateInTest.plusDays(14)})",
            expectedContextId = meldingTypeInTest.id
        )
    )
})
