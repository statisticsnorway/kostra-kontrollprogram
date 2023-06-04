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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraUndersokelseTypeInTest

class Undersokelse02cTest : BehaviorSpec({
    val sut = Undersokelse02c()

    Given("valid context") {
        forAll(
            row("individ without sluttDato", kostraIndividInTest),
            row(
                "individ with sluttDato, without melding", kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1)
                )
            ),
            row(
                "melding without undersokelse",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(kostraMeldingTypeInTest)
                )
            ),
            row(
                "undersokelse without sluttDato",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest
                        )
                    )
                )
            ),
            row(
                "undersokelse with sluttDato",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(1)
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
                "undersokelse with sluttDato after individ sluttdato",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(2)
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
                            it.messageText shouldBe "Undersøkelse ($id). Undersøkelsens sluttdato " +
                                    "($sluttDato) er etter individets sluttdato (${currentContext.sluttDato})"
                        }
                    }
                }
            }
        }
    }
})
