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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraOpphevelseTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.omsorgLovhjemmelTypeInTest
import no.ssb.kostra.validation.report.Severity

class Lovhjemmel02Test : BehaviorSpec({
    val sut = Lovhjemmel02()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "individ with tiltak, erOmsorgstiltak = false",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
                )
            ),
            row(
                "individ with tiltak, erOmsorgstiltak = true, sluttDato = null",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            sluttDato = null,
                            lovhjemmel = omsorgLovhjemmelTypeInTest
                        )
                    )
                )
            ),
            row(
                "individ with tiltak, erOmsorgstiltak = true, sluttDato !=, opphevelse != null",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            sluttDato = dateInTest,
                            lovhjemmel = omsorgLovhjemmelTypeInTest,
                            opphevelse = kostraOpphevelseTypeInTest
                        )
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
                "individ with tiltak, erOmsorgstiltak = true, sluttDato !=, opphevelse = null",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            sluttDato = dateInTest,
                            lovhjemmel = omsorgLovhjemmelTypeInTest,
                            opphevelse = null
                        )
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
                        it.severity shouldBe Severity.WARNING
                        it.journalId shouldBe currentContext.journalnummer
                        it.contextId shouldBe currentContext.tiltak.first().id
                        it.messageText shouldBe
                                "Lovhjemmel Kontroll 2: Omsorgstiltak med sluttdato krever årsak til opphevelse"
                    }
                }
            }
        }
    }
})
