package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraLovhjemmelTypeInTest

class Lovhjemmel04Test : BehaviorSpec({
    val sut = Lovhjemmel04()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "individ with tiltak with valid lovhjemmel",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(IndividRuleTestData.kostraTiltakTypeInTest)
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
                "invalid kapittel",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.kostraTiltakTypeInTest.copy(
                            lovhjemmel = kostraLovhjemmelTypeInTest.copy(kapittel = "0")
                        )
                    )
                )
            ),
            row(
                "invalid paragraf",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        IndividRuleTestData.kostraTiltakTypeInTest.copy(
                            lovhjemmel = kostraLovhjemmelTypeInTest.copy(paragraf = "0")
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
                        it.contextId shouldBe currentContext.tiltak.first().id

                        with(currentContext.tiltak.first().lovhjemmel) {
                            it.messageText shouldBe
                                    "Tiltak (${currentContext.tiltak.first().id}). Kapittel ($kapittel) eller paragraf " +
                                    "($paragraf) er rapportert med den ugyldige koden 0"
                        }
                    }
                }
            }
        }
    }
})
