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
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak02dTest : BehaviorSpec({
    val sut = Tiltak02d()

    Given("valid context") {
        forAll(
            row("individ with avslutta3112 = '2'", individInTest),
            row(
                "avslutta3112 = '1', no tiltak", individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE
                )
            ),
            row(
                "avslutta3112 = '1', tiltak with sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.minusYears(1).plusDays(1)
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
                "avslutta3112 = '1', tiltak without sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = null
                        )
                    )
                )
            ),
            row(
                "avslutta3112 = '1', tiltak with valid sluttDato, context without sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = null,
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest.minusYears(1).plusDays(1)
                        )
                    )
                )
            ),
            row(
                "avslutta3112 = '1', tiltak with sluttDato after reporting year",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            sluttDato = dateInTest
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
                            it.messageText shouldBe "Tiltak ($id). Individet er avsluttet hos barnevernet og dets tiltak " +
                                    "skal dermed være avsluttet. Sluttdato er ${sluttDato ?: "uoppgitt"}"
                        }
                    }
                }
            }
        }
    }
})
