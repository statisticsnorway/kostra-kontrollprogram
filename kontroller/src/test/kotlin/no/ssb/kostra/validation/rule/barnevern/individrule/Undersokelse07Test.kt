package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest

class Undersokelse07Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Undersokelse07(),
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
                    "undersokelse with konklusjon and vedtaksgrunnlag",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    konklusjon = "1",
                                    vedtaksgrunnlag = mutableListOf(IndividRuleTestData.vedtaksgrunnlagTypeInTest)
                                )
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with konklusjon without vedtaksgrunnlag, konklusjon = 1",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    konklusjon = "1"
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Undersøkelse konkludert med " +
                            "kode 1 skal ha vedtaksgrunnlag"
                ),
                ForAllRowItem(
                    "undersokelse with konklusjon without vedtaksgrunnlag, konklusjon = 2",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    konklusjon = "2"
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Undersøkelse konkludert med " +
                            "kode 2 skal ha vedtaksgrunnlag"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id
        )
    )
})
