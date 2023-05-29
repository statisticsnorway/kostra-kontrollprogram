package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.report.Severity

class Melding02cTest : BehaviorSpec({
    val sut = Melding02c()

    Given("valid context") {
        forAll(
            row("individ without sluttDato", kostraIndividInTest),
            row(
                "individ with sluttDato, without melding", kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1)
                )
            ),
            row(
                "melding without sluttDato",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(kostraMeldingTypeInTest)
                )
            ),
            row(
                "melding with sluttDato equal to individ",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(kostraMeldingTypeInTest.copy(sluttDato = dateInTest.plusDays(1)))
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
                "melding with sluttDato after individ sluttDato",
                kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    melding = mutableListOf(kostraMeldingTypeInTest.copy(sluttDato = dateInTest.plusDays(2)))
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
                            it.messageText shouldBe "Melding ($id). Meldingens sluttdato ($sluttDato) " +
                                    "er etter individets sluttdato (${currentContext.sluttDato})"
                        }
                    }
                }
            }
        }
    }
})
