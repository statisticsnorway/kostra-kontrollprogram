package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ConversionExtensionsKtTest :
    BehaviorSpec({

        Given("Int.toYearWithCentury") {
            data class Case(
                val yearWithoutCentury: Int,
                val expectedYearWithCentury: Int,
            )

            listOf(
                Case(0, 0),
                Case(1, 2001),
                Case(99, 2099),
                Case(100, 100),
            ).forEach { (yearWithoutCentury, expectedYearWithCentury) ->

                When("$yearWithoutCentury $expectedYearWithCentury") {
                    yearWithoutCentury.yearWithCentury().shouldBe(expectedYearWithCentury)
                }
            }
        }
    })
