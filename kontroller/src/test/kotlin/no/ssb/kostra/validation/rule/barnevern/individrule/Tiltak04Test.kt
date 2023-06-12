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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraLovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraOpphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.omsorgLovhjemmelTypeInTest

class Tiltak04Test : BehaviorSpec({
    val sut = Tiltak04()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "tiltak without sluttdato",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
                )
            ),
            row(
                "tiltak with sluttdato, not omsorgstiltak",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1),
                            lovhjemmel = kostraLovhjemmelTypeInTest
                        )
                    )
                )
            ),
            row(
                "tiltak with sluttdato, omsorgstiltak",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            sluttDato = dateInTest.plusDays(1),
                            lovhjemmel = omsorgLovhjemmelTypeInTest,
                            opphevelse = kostraOpphevelseTypeInTest
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
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
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
