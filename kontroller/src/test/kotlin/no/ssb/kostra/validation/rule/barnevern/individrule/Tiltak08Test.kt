package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraOpphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.report.Severity

class Tiltak08Test : BehaviorSpec({
    val sut = Tiltak08()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "individ with opphevelse that does not require presisering",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(kostraTiltakTypeInTest.copy(opphevelse = kostraOpphevelseTypeInTest))
                )
            ),
            row(
                "individ with opphevelse with presisering",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            opphevelse = kostraOpphevelseTypeInTest.copy(kode = "4")
                        )
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
                "individ with opphevelse without presisering",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            opphevelse = kostraOpphevelseTypeInTest.copy(
                                kode = "4",
                                presisering = null
                            )
                        )
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
                            it.messageText shouldBe "Tiltak ($id). Tiltaksopphevelse " +
                                    "(${opphevelse?.kode}) mangler presisering"
                        }
                    }
                }
            }
        }
    }
})
