package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraMelderTypeInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.area.barnevern.individrule.Melder02.Companion.ANDRE_OFFENTLIGE_INSTANSER
import no.ssb.kostra.validation.report.Severity

class Melder02Test : BehaviorSpec({
    val sut = Melder02()

    Given("valid context") {
        forAll(
            row("individ without melding", kostraIndividInTest),
            row(
                "individ with melding without melder",
                kostraIndividInTest.copy(melding = mutableListOf(kostraMeldingTypeInTest))
            ),
            row(
                "individ with melding with melder, kode different from 22",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            melder = mutableListOf(kostraMelderTypeInTest)
                        )
                    )
                )
            ),
            row(
                "individ with melding with melder, kode = 22",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            melder = mutableListOf(
                                kostraMelderTypeInTest.copy(kode = ANDRE_OFFENTLIGE_INSTANSER)
                            )
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
                "individ with melding with melder, kode = 22, presisering missing",
                kostraIndividInTest.copy(
                    melding = mutableListOf(
                        kostraMeldingTypeInTest.copy(
                            melder = mutableListOf(
                                kostraMelderTypeInTest.copy(
                                    kode = ANDRE_OFFENTLIGE_INSTANSER,
                                    presisering = null
                                )
                            )
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
                        it.severity shouldBe Severity.ERROR
                        it.journalId shouldBe currentContext.journalnummer
                        it.contextId shouldBe currentContext.melding.first().id

                        with(currentContext.melding.first().melder.first()) {
                            it.messageText shouldBe "Melder med kode ($kode) mangler presisering"
                        }
                    }
                }
            }
        }
    }
})
