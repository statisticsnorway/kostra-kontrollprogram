package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.report.Severity

class Melding02aTest : BehaviorSpec({
    val sut = Melding02a()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "melding without sluttDato",
                kostraIndividInTest.copy(melding = mutableListOf(kostraMeldingTypeInTest))
            ),
            row(
                "melding with sluttDato after startDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
                    )
                )
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, IndividRuleTestData.argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "melding with sluttDato before startDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
                    )
                )
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, IndividRuleTestData.argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.journalId shouldBe currentContext.journalnummer

                        with(currentContext.melding.first()) {
                            it.contextId shouldBe id
                            it.messageText shouldBe "Melding ($id). Meldingens startdato ($startDato) er etter " +
                                    "meldingens sluttdato ($sluttDato)"
                        }
                    }
                }
            }
        }
    }
})
