package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.report.Severity

class Melding02eTest : BehaviorSpec({
    val sut = Melding02e()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "melding with startDato equal to individ startDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest)
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
                "melding with startDato before individ startDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(startDato = dateInTest.minusDays(1))
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
                        it.contextId shouldBe currentContext.melding.first().id

                        with(currentContext.melding.first()) {
                            it.messageText shouldBe "Melding ($id). Startdato ($startDato) skal være lik eller " +
                                    "etter individets startdato (${currentContext.startDato})"
                        }
                    }
                }
            }
        }
    }
})
