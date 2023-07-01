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
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.vedtaksgrunnlagTypeInTest

class Vedtak02Test : BehaviorSpec({
    val sut = Vedtak02()

    Given("valid context") {
        forAll(
            row("individ without melding", individInTest),
            row(
                "melding without undersokelse",
                individInTest.copy(
                    melding = mutableListOf(meldingTypeInTest)
                )
            ),
            row(
                "undersokelse without vedtaksgrunnlag",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest
                        )
                    )
                )
            ),
            row(
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
            row(
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
                )
            ),
            row(
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
