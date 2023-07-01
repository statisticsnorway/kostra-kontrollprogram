package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02aTest : BehaviorSpec({
    val sut = Tiltak02a()

    Given("valid context") {
        forAll(
            row("individ without tiltak", individInTest),
            row(
                "tiltak without sluttDato",
                individInTest.copy(tiltak = mutableListOf(tiltakTypeInTest))
            ),
            row(
                "tiltak with sluttDato after startDato",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
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
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
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
