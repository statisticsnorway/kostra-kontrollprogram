package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

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
})
