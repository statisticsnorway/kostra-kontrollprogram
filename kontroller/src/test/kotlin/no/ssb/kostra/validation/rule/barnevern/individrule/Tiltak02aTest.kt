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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest

class Tiltak02aTest : BehaviorSpec({
    val sut = Tiltak02a()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "tiltak without sluttDato",
                kostraIndividInTest.copy(tiltak = mutableListOf(kostraTiltakTypeInTest))
            ),
            row(
                "tiltak with sluttDato after startDato",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
                    )
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
                "tiltak with sluttDato before startDato",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
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
                            it.messageText shouldBe "Tiltak ($id). Startdato ($startDato) for tiltaket er " +
                                    "etter sluttdato ($sluttDato) for tiltaket"
                        }
                    }
                }
            }
        }
    }
})
