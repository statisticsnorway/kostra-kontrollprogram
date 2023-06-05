package no.ssb.kostra.validation.rule.barnevern.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.BarnevernTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import java.time.LocalDate

class KostraTiltakTypeExtensionsKtTest : BehaviorSpec({

    Given("isOverlapWithAtLeastThreeMonthsOf") {
        val sut = kostraTiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1))

        forAll(
            row(
                "other with concluded date, is not overlapping",
                kostraTiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(MIN_OVERLAP_IN_DAYS - 1)),
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS - 1),
                false
            ),
            row(
                "other without concluded date, is not overlapping",
                kostraTiltakTypeInTest,
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS - 1),
                false
            ),
            row(
                "other with concluded date, is overlapping",
                kostraTiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1)),
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1),
                true
            ),
            row(
                "other without concluded date, is overlapping",
                kostraTiltakTypeInTest,
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1),
                true
            )
        ) { description, otherMeasure, fallbackDate, expectedResult ->

            When(description) {
                val areOverlapping = sut.isOverlapWithAtLeastThreeMonthsOf(
                    otherMeasure,
                    fallbackDate
                )

                Then("expect expectedResult") {
                    areOverlapping shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraTiltakType#toStartDateEndDateRange(LocalDate)") {

        forAll(
            row(
                "tiltak with sluttDato",
                kostraTiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(2)),
                dateInTest.plusDays(3)
            ),
            row(
                "tiltak without sluttDato",
                kostraTiltakTypeInTest,
                dateInTest.plusDays(3)
            )
        ) { description, sut, fallbackDate ->

            When(description) {
                val range = sut.toStartDateEndDateRange(fallbackDate)

                Then("expect expectedResult") {
                    range.start shouldBe sut.startDato
                    range.endInclusive shouldBe (sut.sluttDato ?: fallbackDate)
                }
            }
        }
    }

    Given("ClosedRange<LocalDate>#areOverlapping(LocalDate)") {

        val sut = LocalDate.now().rangeTo(LocalDate.now().plusDays(3))

        forAll(
            row(
                "other range before sut",
                dateInTest.minusDays(4).rangeTo(dateInTest.minusDays(1)),
                false
            ),
            row(
                "other range 2 days into sut",
                dateInTest.minusDays(2).rangeTo(dateInTest.plusDays(2)),
                true
            ),
            row(
                "other range is equal to sut",
                dateInTest.rangeTo(dateInTest.plusDays(3)),
                true
            ),
            row(
                "other range is after to sut",
                dateInTest.plusDays(4).rangeTo(dateInTest.plusDays(7)),
                false
            )
        ) { description, otherRange, expectedResult ->

            When(description) {
                val areOverlapping = sut.areOverlapping(otherRange)

                Then("expect expectedResult") {
                    areOverlapping shouldBe expectedResult
                }
            }
        }
    }
})
