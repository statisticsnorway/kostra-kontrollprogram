package no.ssb.kostra.program.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.util.ConversionUtils.toInt
import no.ssb.kostra.validation.report.Severity

class ConversionUtilsTest : BehaviorSpec({

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
