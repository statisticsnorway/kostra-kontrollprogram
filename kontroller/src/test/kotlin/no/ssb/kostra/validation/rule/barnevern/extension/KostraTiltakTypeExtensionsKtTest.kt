package no.ssb.kostra.validation.rule.barnevern.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.BarnevernTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.MEASURE_CATEGORY_CODE_8_2
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class KostraTiltakTypeExtensionsKtTest : BehaviorSpec({

    Given("erPlasseringsTiltak") {
        forAll(
            row(
                "kategori#kode mismatch, expect false",
                tiltakTypeInTest,
                false
            ),
            row(
                "kategori#kode 1.1, expect true",
                tiltakTypeInTest.copy(kategori = kategoriTypeInTest.copy(kode = "1.1")),
                true
            ),
            row(
                "kategori#kode 2.1, expect true",
                tiltakTypeInTest.copy(kategori = kategoriTypeInTest.copy(kode = "2.1")),
                true
            ),
            row(
                "kategori#kode 8.2, expect true",
                tiltakTypeInTest.copy(kategori = kategoriTypeInTest.copy(kode = MEASURE_CATEGORY_CODE_8_2)),
                true
            )
        ) { description, measure, expected ->

            When(description) {
                val result = measure.erPlasseringsTiltak()

                Then("result should be as expected") {
                    result shouldBe expected
                }
            }
        }
    }

    Given("overlapInNumberOfDays") {
        forAll(
            row(
                "overlapping with one day, both measures without sluttDato",
                tiltakTypeInTest,
                tiltakTypeInTest,
                dateInTest.plusDays(1)
            ),
            row(
                "overlapping with one day, both measures with sluttDato",
                tiltakTypeInTest.copy(
                    startDato = dateInTest.plusDays(1),
                    sluttDato = dateInTest.plusDays(2)
                ),
                tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(2)),
                dateInTest
            ),
            row(
                "first without sluttDato",
                tiltakTypeInTest,
                tiltakTypeInTest.copy(
                    startDato = dateInTest.plusDays(1),
                    sluttDato = dateInTest.plusDays(3)
                ),
                dateInTest.plusDays(2)
            ),
            row(
                "second without sluttDato",
                tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(4)),
                tiltakTypeInTest,
                dateInTest.plusDays(1)
            )
        ) { description, sut, other, fallbackDate ->
            When(description) {
                val overlapInNumberOfDays = sut.overlapInNumberOfDays(other, fallbackDate)

                Then("overlapInNumberOfDays should be 1") {
                    overlapInNumberOfDays shouldBe 1L
                }
            }
        }
    }

    Given("isOverlapWithAtLeastThreeMonthsOf") {
        val sut = tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1))

        forAll(
            row(
                "other with concluded date, is not overlapping",
                tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(MIN_OVERLAP_IN_DAYS - 1)),
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS - 1),
                false
            ),
            row(
                "other without concluded date, is not overlapping",
                tiltakTypeInTest,
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS - 1),
                false
            ),
            row(
                "other with concluded date, is overlapping",
                tiltakTypeInTest.copy(sluttDato = dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1)),
                dateInTest.plusDays(MIN_OVERLAP_IN_DAYS + 1),
                true
            ),
            row(
                "other without concluded date, is overlapping",
                tiltakTypeInTest,
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
})
