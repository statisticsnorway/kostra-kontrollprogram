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

class Individ03Test : BehaviorSpec({
    val sut = Individ03()

    Given("valid context") {
        forAll(
            row("individ with valid SSN", kostraIndividInTest),
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
                "individ with invalid SSN",
                kostraIndividInTest.copy(fodselsnummer = "~fodselsnummer~"),
                "Feil i fødselsnummer. Kan ikke identifisere individet."
            ),
            row(
                "individ with invalid DUF-nummer",
                kostraIndividInTest.copy(
                    fodselsnummer = null,
                    duFnummer = "~duFnummer~"
                ),
                "DUF-nummer mangler. Kan ikke identifisere individet."
            ),
            row(
                "individ with neither SSN nor DUF-nummer",
                kostraIndividInTest.copy(
                    fodselsnummer = null,
                    duFnummer = null
                ),
                "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."
            )
        ) { description, currentContext, expectedErrorMessage ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.contextId shouldBe currentContext.id
                        it.messageText shouldBe expectedErrorMessage
                    }
                }
            }
        }
    }
})
