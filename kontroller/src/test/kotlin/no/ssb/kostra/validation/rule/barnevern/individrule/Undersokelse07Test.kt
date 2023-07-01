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

class Undersokelse07Test : BehaviorSpec({
    val sut = Undersokelse07()

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
                "undersokelse without konklusjon",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest
                        )
                    )
                )
            ),
            row(
                "undersokelse with unrelated konklusjon",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(konklusjon = "~konklusjon~")
                        )
                    )
                )
            ),
            row(
                "undersokelse with konklusjon and vedtaksgrunnlag",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                konklusjon = "1",
                                vedtaksgrunnlag = mutableListOf(vedtaksgrunnlagTypeInTest)
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
                "undersokelse with konklusjon without vedtaksgrunnlag, konklusjon = 1",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                konklusjon = "1"
                            )
                        )
                    )
                )
            ),
            row(
                "undersokelse with konklusjon without vedtaksgrunnlag, konklusjon = 2",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                konklusjon = "2"
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
                            it.messageText shouldBe "Undersøkelse ($id). Undersøkelse konkludert med kode " +
                                    "$konklusjon skal ha vedtaksgrunnlag"
                        }
                    }
                }
            }
        }
    }
})
