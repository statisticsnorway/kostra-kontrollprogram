package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.argumentsInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraKategoriTypeInTest as kostraKategoriTypeInTest1

class Tiltak07Test : BehaviorSpec({
    val sut = Tiltak07()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "individ with kategori that does not require presisering",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
                )
            ),
            row(
                "individ with kategori with presisering",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            kategori = kostraKategoriTypeInTest1.copy(kode = "1.99")
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
                "individ with kategori without presisering",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            kategori = kostraKategoriTypeInTest1.copy(
                                kode = "1.99",
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
                            it.messageText shouldBe  "Tiltak ($id). Tiltakskategori (${kategori.kode}) mangler presisering"
                        }
                    }
                }
            }
        }
    }
})
