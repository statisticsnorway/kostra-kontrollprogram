package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.vedtaksgrunnlagTypeInTest

class Vedtak02Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Vedtak02(),
            forAllRows = listOf(
                ForAllRowItem("individ without melding", individInTest),
                ForAllRowItem(
                    "melding without undersokelse",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "undersokelse without vedtaksgrunnlag",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with vedtaksgrunnlag, unrelated kode",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    vedtaksgrunnlag = mutableListOf(
                                        vedtaksgrunnlagTypeInTest.copy(presisering = null)
                                    )
                                )
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with vedtaksgrunnlag with presisering-",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    vedtaksgrunnlag = mutableListOf(
                                        vedtaksgrunnlagTypeInTest.copy(kode = "18")
                                    )
                                )
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "undersokelse with vedtaksgrunnlag without presisering, kode = 18",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    vedtaksgrunnlag = mutableListOf(
                                        vedtaksgrunnlagTypeInTest.copy(
                                            kode = "18",
                                            presisering = null
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    expectedErrorMessage ="Vedtaksgrunnlag med kode 18 mangler presisering"
                ),
                ForAllRowItem(
                    "undersokelse with vedtaksgrunnlag without presisering, kode = 19",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    vedtaksgrunnlag = mutableListOf(
                                        vedtaksgrunnlagTypeInTest.copy(
                                            kode = "19",
                                            presisering = null
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    expectedErrorMessage ="Vedtaksgrunnlag med kode 19 mangler presisering"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id
        )
    )
})
