package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.Undersokelse03.Companion.CODE_THAT_REQUIRES_CLARIFICATION

class Undersokelse03Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Undersokelse03(),
            forAllRows = listOf(
                ForAllRowItem("individ without melding", individInTest),
                ForAllRowItem(
                    "melding without undersokelse",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "undersokelse without konklusjon",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with unrelated konklusjon",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(konklusjon = "~konklusjon~")
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with konklusjon and presisering",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    konklusjon = CODE_THAT_REQUIRES_CLARIFICATION,
                                    presisering = "~presisering~"
                                )
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "undersokelse with konklusjon without presisering",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    konklusjon = CODE_THAT_REQUIRES_CLARIFICATION,
                                    presisering = null
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Undersøkelse der kode for " +
                            "konklusjon er $CODE_THAT_REQUIRES_CLARIFICATION mangler presisering"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id
        )
    )
})
