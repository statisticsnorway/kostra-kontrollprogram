package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraKategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest

class Tiltak09Test : BehaviorSpec({
    val sut = Tiltak09()

    Given("valid context") {
        forAll(
            row("individ without tiltak", kostraIndividInTest),
            row(
                "individ with single tiltak",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
                )
            ),
            row(
                "individ with single plasseringstiltak",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            kategori = kostraKategoriTypeInTest.copy(kode = "1.1")
                        )
                    )
                )
            ),
            row(
                "two overlapping plasseringstiltak, less than 90 days",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = null,
                            kategori = kostraKategoriTypeInTest.copy(kode = "1.1")
                        ),
                        kostraTiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = dateInTest.minusYears(1).plusDays(89),
                            kategori = kostraKategoriTypeInTest.copy(kode = "1.2")
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
                "two overlapping plasseringstiltak, less than 90 days",
                kostraIndividInTest.copy(
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = null,
                            kategori = kostraKategoriTypeInTest.copy(kode = "1.1")
                        ),
                        kostraTiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = dateInTest.minusYears(1).plusDays(100),
                            kategori = kostraKategoriTypeInTest.copy(kode = "1.2")
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
                            it.messageText shouldBe "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"
                        }
                    }
                }
            }
        }
    }
})
