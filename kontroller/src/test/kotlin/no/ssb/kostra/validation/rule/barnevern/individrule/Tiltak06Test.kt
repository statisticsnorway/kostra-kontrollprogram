package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.validation.rule.barnevern.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraKategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.report.Severity

class Tiltak06Test : BehaviorSpec({
    val sut = Tiltak06()

    Given("valid context") {
        forAll(
            row("individ without fodselsnummer", kostraIndividInTest.copy(fodselsnummer = null)),
            row(
                "individ with fodselsnummer, age below 7",
                kostraIndividInTest.copy(fodselsnummer = generateRandomSSN(
                    dateInTest, dateInTest.plusYears(1))
                )
            ),
            row(
                "individ with fodselsnummer, age above 11, no tiltak",
                kostraIndividInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.plusYears(11),
                        dateInTest.plusYears(12)
                    )
                )
            ),
            row(
                "individ with fodselsnummer, age above 11, tiltak with kategori#kode different from 4.2",
                kostraIndividInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.plusYears(11),
                        dateInTest.plusYears(12)
                    ),
                    tiltak = mutableListOf(kostraTiltakTypeInTest)
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
                "individ with fodselsnummer, age above 11, tiltak with kategori#kode different from 4.2",
                kostraIndividInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.plusYears(11),
                        dateInTest.plusYears(12)
                    ),
                    tiltak = mutableListOf(
                        kostraTiltakTypeInTest.copy(
                        kategori = kostraKategoriTypeInTest.copy(kode = "4.2")
                    ))
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
                        it.journalId shouldBe currentContext.journalnummer

                        with(currentContext.tiltak.first()) {
                            it.contextId shouldBe id
                            it.messageText shouldStartWith  "Tiltak ($id). Barnet er over 11 år og i SFO. " +
                                    "Barnets alder er"
                        }
                    }
                }
            }
        }
    }
})
