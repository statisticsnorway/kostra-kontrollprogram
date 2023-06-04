package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ11Test : BehaviorSpec({
    val sut = Individ11()

    Given("valid context") {
        forAll(
            row("individ with normal fodselsnummer", kostraIndividInTest),
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
            row("individ without fodselsnummer", kostraIndividInTest.copy(fodselsnummer = null)),
            row("individ with empty fodselsnummer", kostraIndividInTest.copy(fodselsnummer = "")),
            row("individ with blank fodselsnummer", kostraIndividInTest.copy(fodselsnummer = "  ")),
            row("individ with invalid fodselsnummer", kostraIndividInTest.copy(fodselsnummer = "12345612345")),
            row("individ with 555555 fodselsnummer", kostraIndividInTest.copy(fodselsnummer = "123456555555")),
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.journalId shouldBe currentContext.journalnummer
                        it.contextId shouldBe currentContext.id
                        it.messageText shouldBe "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
                    }
                }
            }
        }
    }
})
