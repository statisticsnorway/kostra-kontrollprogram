package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import java.time.LocalDate

class Individ07Test : BehaviorSpec({
    val sut = Individ07()

    Given("valid context") {
        forAll(
            row("individ without fodselsnummer", kostraIndividInTest.copy(fodselsnummer = null)),
            row(
                "individ with invalid fodselsnummer",
                kostraIndividInTest.copy(fodselsnummer = "12345612345")
            ),
            row(
                "individ with fodselsnummer, age below 25",
                kostraIndividInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        LocalDate.now().minusYears(1),
                        LocalDate.now()
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
        val invalidContext = kostraIndividInTest.copy(
            fodselsnummer = generateRandomSSN(
                LocalDate.now().minusYears(27),
                LocalDate.now().minusYears(26),
            )
        )

        When("validate") {
            val reportEntryList =
                sut.validate(invalidContext, argumentsInTest)

            Then("expect non-null result") {
                reportEntryList.shouldNotBeNull()
                reportEntryList.size shouldBe 1

                assertSoftly(reportEntryList.first()) {
                    it.severity shouldBe Severity.ERROR
                    it.contextId shouldBe invalidContext.id
                    it.messageText shouldStartWith "Individet er"
                    it.messageText shouldEndWith "år og skal avsluttes som klient"
                }
            }
        }
    }
})
