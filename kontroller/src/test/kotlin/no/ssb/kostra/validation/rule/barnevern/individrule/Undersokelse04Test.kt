package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest

class Undersokelse04Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Undersokelse04(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id,
            ForAllRowItem("individ without melding", individInTest),
            ForAllRowItem(
                "melding without undersokelse",
                individInTest.copy(
                    melding = mutableListOf(meldingTypeInTest)
                )
            ),
            ForAllRowItem(
                "undersokelse without sluttDato",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest
                        )
                    )
                )
            ),
            ForAllRowItem(
                "undersokelse with sluttDato and konklusjon",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(1),
                                konklusjon = "~konklusjon~"
                            )
                        )
                    )
                )
            ),
            ForAllRowItem(
                "undersokelse with sluttDato without konklusjon",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(1),
                                konklusjon = null
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). " +
                        "Avsluttet undersøkelse mangler konklusjon"
            )
        )
    )
})
