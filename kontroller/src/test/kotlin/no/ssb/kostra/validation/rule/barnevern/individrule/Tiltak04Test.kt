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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.opphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.omsorgLovhjemmelTypeInTest

class Tiltak04Test : BehaviorSpec({
    val sut = Tiltak04()

    Given("valid context") {
        forAll(
            row("individ without tiltak", individInTest),
            row(
                "tiltak without sluttdato",
                individInTest.copy(
                    tiltak = mutableListOf(tiltakTypeInTest)
                )
            ),
            row(
                "tiltak with sluttdato, not omsorgstiltak",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1),
                            lovhjemmel = lovhjemmelTypeInTest
                        )
                    )
                )
            ),
            row(
                "tiltak with sluttdato, omsorgstiltak",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1),
                            lovhjemmel = omsorgLovhjemmelTypeInTest,
                            opphevelse = opphevelseTypeInTest
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
                "tiltak with sluttdato, omsorgstiltak, opphevelse missing",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1),
                            lovhjemmel = omsorgLovhjemmelTypeInTest,
                            opphevelse = null
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
                        it.severity shouldBe Severity.WARNING

                        with(currentContext.tiltak.first()) {
                            it.contextId shouldBe id
                            it.messageText shouldBe "Tiltak ($id). Omsorgstiltak med sluttdato ($sluttDato) " +
                                    "krever kode for opphevelse"
                        }
                    }
                }
            }
        }
    }
})
