package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RandomUtils.generateRandomDuf
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import java.time.Year

class Individ19Test : BehaviorSpec({
    val sut = Individ19()

    Given("valid context") {
        forAll(
            row("individ without DUF-nummer", kostraIndividInTest),
            row(
                "individ with valid DUF-nummer", kostraIndividInTest.copy(
                    fodselsnummer = null,
                    duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1)
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
                "individ with invalid DUF-nummer", kostraIndividInTest.copy(
                    fodselsnummer = null,
                    duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1).take(10)
                )
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.contextId shouldBe currentContext.id
                        it.messageText shouldBe "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer."
                    }
                }
            }
        }
    }
})
