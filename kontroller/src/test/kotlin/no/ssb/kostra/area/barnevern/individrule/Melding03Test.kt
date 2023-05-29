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

class Melding03Test : BehaviorSpec({
    val sut = Melding03()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "melding without sluttDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest)
                )
            ),
            row(
                "melding with sluttDato less than 8 days from startDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest.copy(sluttDato = dateInTest.plusDays(4)))
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
                "melding with sluttDato more than 8 days from startDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest.copy(sluttDato = dateInTest.plusDays(14)))
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
                        it.journalId shouldBe currentContext.journalnummer
                        it.contextId shouldBe currentContext.melding.first().id

                        with(currentContext.melding.first()) {
                            it.messageText shouldBe "Melding ($id). Fristoverskridelse på behandlingstid for melding,  " +
                                    "($startDato -> $sluttDato)"
                        }
                    }
                }
            }
        }
    }
})
