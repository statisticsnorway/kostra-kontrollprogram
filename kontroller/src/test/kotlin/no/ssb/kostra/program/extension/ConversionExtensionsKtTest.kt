package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity

class ConversionExtensionsKtTest : BehaviorSpec({

    Given("Int.toYearWithCentury") {
        forAll(
            row(0, 0),
            row(1, 2001),
            row(99, 2099),
            row(100, 100)
        ) { yearWithoutCentury, expectedYearWithCentury ->

            When("$yearWithoutCentury $expectedYearWithCentury") {
                yearWithoutCentury.yearWithCentury().shouldBe(expectedYearWithCentury)
            }
        }
    }

    Given("Severity.toInt") {
        forAll(
            row(Severity.WARNING, 1),
            row(Severity.ERROR, 2),
            row(Severity.INFO, 0),
        ) { sut, expectedValue ->

            When(sut.name) {
                val result = sut.toInt()

                Then("result should be as expected") {
                    result shouldBe expectedValue
                }
            }
        }
    }
})
