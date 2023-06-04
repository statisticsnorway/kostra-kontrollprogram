package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraLovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import java.time.LocalDate

class Lovhjemmel03Test : BehaviorSpec({
    val sut = Lovhjemmel03()

    Given("valid context") {
        forAll(
            row("individ without fodselsnummer", kostraIndividInTest.copy(fodselsnummer = null)),
            row("individ with fodselsnummer, age below eighteen, no tiltak", kostraIndividInTest),
            row(
                "age above eighteen, no omsorgstiltak",
                kostraIndividInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        LocalDate.now().minusYears(20),
                        LocalDate.now().minusYears(19)
                    ),
                    tiltak = mutableListOf(kostraTiltakTypeInTest.copy(lovhjemmel = kostraLovhjemmelTypeInTest))
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
                "age above eighteen, no omsorgstiltak",
                kostraIndividInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        LocalDate.now().minusYears(20),
                        LocalDate.now().minusYears(19)
                    ),
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
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
                        it.messageText shouldStartWith
                                "Tiltak (${currentContext.tiltak.first().id}). Individet er"
                    }
                }
            }
        }
    }
})
