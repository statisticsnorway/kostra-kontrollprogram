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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.opphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak08Test : BehaviorSpec({
    val sut = Tiltak08()

    Given("valid context") {
        forAll(
            row("individ without tiltak", individInTest),
            row(
                "individ with opphevelse that does not require presisering",
                individInTest.copy(
                    tiltak = mutableListOf(tiltakTypeInTest.copy(opphevelse = opphevelseTypeInTest))
                )
            ),
            row(
                "individ with opphevelse with presisering",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            opphevelse = opphevelseTypeInTest.copy(kode = "4")
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
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            opphevelse = opphevelseTypeInTest.copy(
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
