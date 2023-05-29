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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraUndersokelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraVedtaksgrunnlagTypeInTest

class Undersokelse07Test : BehaviorSpec({
    val sut = Undersokelse07()

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
                "undersokelse without konklusjon",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest
                        )
                    )
                )
            ),
            row(
                "undersokelse with unrelated konklusjon",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(konklusjon = "~konklusjon~")
                        )
                    )
                )
            ),
            row(
                "undersokelse with konklusjon and vedtaksgrunnlag",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                konklusjon = "1",
                                vedtaksgrunnlag = mutableListOf(kostraVedtaksgrunnlagTypeInTest)
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
                "undersokelse with konklusjon without vedtaksgrunnlag",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                konklusjon = "1"
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
                        it.journalId shouldBe currentContext.journalnummer

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
