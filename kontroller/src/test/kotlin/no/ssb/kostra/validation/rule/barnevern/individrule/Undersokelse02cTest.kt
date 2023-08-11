package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest

class Undersokelse02cTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Undersokelse02c(),
            forAllRows = listOf(
                ForAllRowItem("individ without sluttDato", individInTest),
                ForAllRowItem(
                    "individ with sluttDato, without melding", individInTest.copy(
                        sluttDato = dateInTest.plusDays(1)
                    )
                ),
                ForAllRowItem(
                    "melding without undersokelse",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "undersokelse without sluttDato",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with sluttDato",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    sluttDato = dateInTest.plusDays(1)
                                )
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "undersokelse with sluttDato after individ sluttdato",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    sluttDato = dateInTest.plusDays(2)
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Undersøkelsens sluttdato " +
                            "(${dateInTest.plusDays(2)}) er etter individets " +
                            "sluttdato (${dateInTest.plusDays(1)})"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id
        )
    )
})
