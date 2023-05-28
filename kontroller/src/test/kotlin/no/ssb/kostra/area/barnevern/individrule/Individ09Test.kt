package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.report.Severity

class Individ09Test : BehaviorSpec({
    val sut = Individ09()

    Given("valid context") {
        forAll(
            row(
                "individ without bydelsnummer",
                kostraIndividInTest.copy(bydelsnummer = null),
                argumentsInTest.copy(region = "123400")
            ),
            row(
                "individ with bydelsnummer, Oslo",
                kostraIndividInTest,
                argumentsInTest
            ),
        ) { description, currentContext, arguments ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, arguments)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        val invalidContext = kostraIndividInTest.copy(bydelsnummer = null)

        When("validate") {
            val reportEntryList = sut.validate(invalidContext, argumentsInTest)

            Then("expect non-null result") {
                reportEntryList.shouldNotBeNull()
                reportEntryList.size shouldBe 1

                assertSoftly(reportEntryList.first()) {
                    it.severity shouldBe Severity.ERROR
                    it.journalId shouldBe invalidContext.journalnummer
                    it.contextId shouldBe invalidContext.id
                    it.messageText shouldBe  "Filen mangler bydelsnummer."
                }
            }
        }
    }
})
