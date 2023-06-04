package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.AvgiverRuleTestData.kostraAvgiverTypeInTest

class Avgiver06Test : BehaviorSpec({
    val sut = Avgiver06()

    Given("valid context") {
        forAll(
            row(
                "avgiver with kommunenavn",
                kostraAvgiverTypeInTest,
                argumentsInTest
            )
        ) { description, context, arguments ->

            When(description) {
                val reportEntryList = sut.validate(context, arguments)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "avgiver with empty kommunenavn",
                kostraAvgiverTypeInTest.copy(kommunenavn = ""),
                argumentsInTest
            ),
            row(
                "avgiver with blank kommunenavn",
                kostraAvgiverTypeInTest.copy(kommunenavn = "   "),
                argumentsInTest
            )
        ) { description, context, arguments ->

            When(description) {
                val reportEntryList = sut.validate(context, arguments)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Filen mangler kommunenavn."
                    }
                }
            }
        }
    }
})
