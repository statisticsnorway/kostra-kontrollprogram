package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.xsd.KostraUndersokelseType
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraUndersokelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraVedtaksgrunnlagTypeInTest

class Vedtak02Test : BehaviorSpec({
    val sut = Vedtak02()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "melding without undersokelse",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest)
                )
            ),
            row(
                "undersokelse without vedtaksgrunnlag",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest
                        )
                    )
                )
            ),
            row(
                "undersokelse with vedtaksgrunnlag, unrelated kode",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                vedtaksgrunnlag = mutableListOf(
                                    kostraVedtaksgrunnlagTypeInTest.copy(presisering = null)
                                )
                            )
                        )
                    )
                )
            ),
            row(
                "undersokelse with vedtaksgrunnlag with presisering-",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                vedtaksgrunnlag = mutableListOf(
                                    kostraVedtaksgrunnlagTypeInTest.copy(kode = "18")
                                )
                            )
                        )
                    )
                )
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "undersokelse with vedtaksgrunnlag without presisering, kode = 18",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                vedtaksgrunnlag = mutableListOf(
                                    kostraVedtaksgrunnlagTypeInTest.copy(
                                        kode = "18",
                                        presisering = null
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            row(
                "undersokelse with vedtaksgrunnlag without presisering, kode = 19",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                vedtaksgrunnlag = mutableListOf(
                                    kostraVedtaksgrunnlagTypeInTest.copy(
                                        kode = "19",
                                        presisering = null
                                    )
                                )
                            )
                        )
                    )
                )
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR

                        with(currentContext.melding.first().undersokelse as KostraUndersokelseType) {
                            it.contextId shouldBe id
                            it.messageText shouldBe
                                    "Vedtaksgrunnlag med kode ${vedtaksgrunnlag.first().kode} mangler presisering"
                        }
                    }
                }
            }
        }
    }
})
