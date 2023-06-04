package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.xsd.KostraUndersokelseType
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraUndersokelseTypeInTest
import java.time.LocalDate

class Undersokelse08Test : BehaviorSpec({
    val sut = Undersokelse08()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "melding without undersokelse",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest)
                )
            ),
            row(
                "undersokelse with startDato before June with sluttDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                startDato = LocalDate.of(argumentsInTest.aargang.toInt(), 5, 1),
                                sluttDato = LocalDate.of(argumentsInTest.aargang.toInt(), 8, 1)
                            )
                        )
                    )
                )
            ),
            row(
                "undersokelse with startDato after June, without sluttDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                startDato = LocalDate.of(argumentsInTest.aargang.toInt(), 7, 1)
                            )
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
                "undersokelse with startDato before June without sluttDato",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            undersokelse = kostraUndersokelseTypeInTest.copy(
                                startDato = LocalDate.of(argumentsInTest.aargang.toInt(), 5, 1)
                            )
                        )
                    )
                )
            ),
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.journalId shouldBe currentContext.journalnummer

                        with(currentContext.melding.first().undersokelse as KostraUndersokelseType) {
                            it.contextId shouldBe id
                            it.messageText shouldBe "Undersøkelse ($id). Undersøkelsen startet $startDato " +
                                    "og skal konkluderes da den har pågått i mer enn 6 måneder"
                        }
                    }
                }
            }
        }
    }
})
