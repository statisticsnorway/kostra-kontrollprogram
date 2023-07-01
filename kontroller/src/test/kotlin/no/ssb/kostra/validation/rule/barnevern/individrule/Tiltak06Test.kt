package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak06Test : BehaviorSpec({
    val sut = Tiltak06()

    Given("valid context") {
        forAll(
            row("individ without fodselsnummer", individInTest.copy(fodselsnummer = null)),
            row(
                "individ with invalid fodselsnummer",
                individInTest.copy(fodselsnummer = "12345612345")
            ),
            row(
                "individ with fodselsnummer, age below 7",
                individInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.minusYears(1),
                        dateInTest
                    )
                )
            ),
            row(
                "individ with fodselsnummer, age above 11, no tiltak",
                individInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.minusYears(12),
                        dateInTest.minusYears(11)
                    )
                )
            ),
            row(
                "individ with fodselsnummer, age above 11, tiltak with kategori#kode different from 4.2",
                individInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.minusYears(12),
                        dateInTest.minusYears(11)
                    ),
                    tiltak = mutableListOf(tiltakTypeInTest)
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
                "individ with fodselsnummer, age above 11, tiltak with kategori#kode equal to 4.2",
                individInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        dateInTest.minusYears(13),
                        dateInTest.minusYears(12)
                    ),
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            kategori = kategoriTypeInTest.copy(kode = "4.2")
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
                            it.messageText shouldStartWith "Tiltak ($id). Barnet er over 11 år og i SFO. " +
                                    "Barnets alder er"
                        }
                    }
                }
            }
        }
    }
})
