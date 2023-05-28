package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.DUF_NUMBER_IN_TEST
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.report.Severity

class Individ03Test : BehaviorSpec({
    val sut = Individ03()

    Given("valid context") {
        forAll(
            row("individ with valid SSN", kostraIndividInTest),
            row(
                "individ with valid DUF-nummer", kostraIndividInTest.copy(
                    fodselsnummer = null,
                    duFnummer = DUF_NUMBER_IN_TEST
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
                        it.journalId shouldBe currentContext.journalnummer
                        it.messageText shouldBe expectedErrorMessage
                    }
                }
            }
        }
    }
})
