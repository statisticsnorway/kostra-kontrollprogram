package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.report.Severity

class Tiltak02eTest : BehaviorSpec({
    val sut = Tiltak02e()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "tiltak with startDato equal to individ startDato",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
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
                "plan with startDato before individ startDato",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(startDato = dateInTest.minusDays(1))
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

                        with(currentContext.tiltak.first()) {
                            it.contextId shouldBe id
                            it.messageText shouldBe "Tiltak ($id). StartDato ($startDato) skal være lik eller etter " +
                                    "individets startdato (${currentContext.startDato})"
                        }
                    }
                }
            }
        }
    }
})
