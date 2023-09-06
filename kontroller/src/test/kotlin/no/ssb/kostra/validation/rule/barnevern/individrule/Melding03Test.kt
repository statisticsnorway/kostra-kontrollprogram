package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding03Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Melding03(),
            expectedSeverity = Severity.WARNING,
            expectedContextId = meldingTypeInTest.id,
            ForAllRowItem(
                "individ without melding",
                individInTest
            ),
            ForAllRowItem(
                "melding without sluttDato",
                individInTest.copy(
                    melding = mutableListOf(meldingTypeInTest)
                )
            ),
            ForAllRowItem(
                "melding with sluttDato less than 8 days from startDato",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(4))
                    )
                )
            ),
            ForAllRowItem(
                "melding with sluttDato more than 8 days from startDato",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(14))
                    )
                ),
                expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Fristoverskridelse på behandlingstid " +
                        "for melding, (${meldingTypeInTest.startDato} -> ${dateInTest.plusDays(14)})"
            )
        )
    )
})
