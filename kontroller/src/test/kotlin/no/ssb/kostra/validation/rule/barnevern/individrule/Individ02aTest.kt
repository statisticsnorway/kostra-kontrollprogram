package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ02aTest : BehaviorSpec({
    val sut = Individ02a()

    Given("valid context") {
        forAll(
            row("individ without endDate", kostraIndividInTest),
            row("individ with valid endDate", kostraIndividInTest.copy(sluttDato = dateInTest.plusDays(1))),
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
        val invalidContext = kostraIndividInTest.copy(sluttDato = dateInTest.minusDays(1))

        When("validate") {
            val reportEntryList = sut.validate(invalidContext, argumentsInTest)

            Then("expect non-null result") {
                reportEntryList.shouldNotBeNull()
                reportEntryList.size shouldBe 1

                assertSoftly(reportEntryList.first()) {
                    it.severity shouldBe Severity.ERROR
                    it.journalId shouldBe invalidContext.journalnummer
                    it.contextId shouldBe invalidContext.id
                    it.messageText shouldContain "Individets startdato"
                }
            }
        }
    }
})
