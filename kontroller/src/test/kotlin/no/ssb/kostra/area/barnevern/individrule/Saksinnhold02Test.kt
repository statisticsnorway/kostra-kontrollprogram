package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraSaksinnholdTypeInTest
import no.ssb.kostra.validation.report.Severity

class Saksinnhold02Test : BehaviorSpec({
    val sut = Saksinnhold02()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "melding without saksinnhold",
                kostraIndividInTest.copy(
                    melding = mutableListOf(kostraMeldingTypeInTest)
                )
            ),
            row(
                "melding with saksinnhold, kode does not require presisering",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            sluttDato = dateInTest,
                            konklusjon = "1",
                            saksinnhold = mutableListOf(kostraSaksinnholdTypeInTest)
                        )
                    )
                )
            ),
            row(
                "melding with saksinnhold, kode does require presisering",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            sluttDato = dateInTest,
                            konklusjon = "1",
                            saksinnhold = mutableListOf(kostraSaksinnholdTypeInTest.copy(kode = "18"))
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
                "melding with saksinnhold, kode does require presisering",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            sluttDato = dateInTest,
                            konklusjon = "1",
                            saksinnhold = mutableListOf(
                                kostraSaksinnholdTypeInTest.copy(
                                    kode = "18",
                                    presisering = null
                                )
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

                        with(currentContext.melding.first()) {
                            it.contextId shouldBe id
                            it.messageText shouldBe "Saksinnhold med kode (${saksinnhold.first().kode}) mangler presisering"
                        }
                    }
                }
            }
        }
    }
})
