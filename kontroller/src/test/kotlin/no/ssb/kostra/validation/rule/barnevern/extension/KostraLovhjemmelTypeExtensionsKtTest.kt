package no.ssb.kostra.validation.rule.barnevern.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barnevern.xsd.KostraLovhjemmelType
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL2021
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest

class KostraLovhjemmelTypeExtensionsKtTest : BehaviorSpec({
    Given("KostraLovhjemmelType.matches") {

        forAll(
            row(
                "instance matches ref, all props set",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    ledd = "1",
                    bokstav = "4",
                    punktum = "6"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    ledd = "1",
                    bokstav = "4",
                    punktum = "6"
                ),
                true
            ),
            row(
                "instance matches ref, min props set",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4"
                ),
                true
            ),
            row(
                "instance matches ref, min props set #2",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                ),
                true
            ),
            row(
                "instance does not match, ledd missing",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    ledd = "1"
                ),
                false
            ),
            row(
                "instance does not match, ledd wrong",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    ledd = "4"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    ledd = "1"
                ),
                false
            ),
            row(
                "instance does not match, bokstav missing",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    bokstav = "a"
                ),
                false
            ),
            row(
                "instance does not match, wrong bokstav",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    bokstav = "a"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    bokstav = "b"
                ),
                false
            ),
            row(
                "instance does not match, punktum missing",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    punktum = "1"
                ),
                false
            ),
            row(
                "instance does not match, wrong punktum",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    punktum = "4"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    punktum = "1"
                ),
                false
            ),
            row(
                "instance does not match, excessive punktum",
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4",
                    punktum = "1"
                ),
                KostraLovhjemmelType(
                    lov = BVL1992,
                    kapittel = "2",
                    paragraf = "4"
                ),
                true
            )
        ) { description, thisKostraLovhjemmelType, other, expectedResult ->

            When(description) {
                val result = thisKostraLovhjemmelType.matches(other)

                Then("result should be expected result") {
                    result shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraLovhjemmelType.isEqualTo") {

        forAll(
            row(
                "instances are equal",
                lovhjemmelTypeInTest,
                lovhjemmelTypeInTest,
                true
            ),
            row(
                "instances are different, lov",
                lovhjemmelTypeInTest,
                lovhjemmelTypeInTest.copy(lov = BVL2021),
                false
            ),
            row(
                "instances are different, kapittel",
                lovhjemmelTypeInTest,
                lovhjemmelTypeInTest.copy(kapittel = "~som value~"),
                false
            ),
            row(
                "instances are different, paragraf",
                lovhjemmelTypeInTest,
                lovhjemmelTypeInTest.copy(paragraf = "~som value~"),
                false
            )
        ) { description, thisCollection, otherCollection, expectedResult ->

            When(description) {
                val result = thisCollection.isEqualTo(otherCollection)

                Then("result should be expected result") {
                    result shouldBe expectedResult
                }
            }
        }
    }
})
