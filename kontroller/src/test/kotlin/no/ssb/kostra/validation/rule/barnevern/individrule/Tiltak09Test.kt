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
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak09Test : BehaviorSpec({
    val sut = Tiltak09()

    Given("valid context") {
        forAll(
            row("individ without tiltak", individInTest),
            row(
                "individ with single tiltak",
                individInTest.copy(
                    tiltak = mutableListOf(tiltakTypeInTest)
                )
            ),
            row(
                "individ with single plasseringstiltak",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        )
                    )
                )
            ),
            row(
                "two overlapping plasseringstiltak, less than 90 days",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = dateInTest.minusYears(1).plusDays(89),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
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
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = dateInTest.minusYears(1).plusDays(100),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
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
