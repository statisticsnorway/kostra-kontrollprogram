package gradletask

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.Code

class KlassModelsTest :
    BehaviorSpec({
        Given("KlassCodes") {
            val sut = KlassCodes(
                codes = listOf(
                    KlassCode("1", "Ja"),
                    KlassCode("2", "Nei"),
                )
            )
            When("converting KlassCodes to CodeList") {
                val result = sut.toCodeList()
                val expected = listOf(
                    Code("1", "Ja"),
                    Code("2", "Nei"),
                )

                Then("the result should be the expected CodeList") {
                    result shouldBe expected
                }
            }
        }

        Given("KlassCorrespondence") {
            val sut = KlassCorrespondence(
                correspondenceItems = listOf(
                    KlassCorrespondenceItem("1", "Ja","1", "Ja"),
                    KlassCorrespondenceItem("2", "Nei","2", "Nei"),
                )
            )
            When("converting KlassCodes to CodeList") {
                val result = sut.toCodePairList()
                val expected = listOf(
                    Code("1", "Ja") to Code("1", "Ja"),
                    Code("2", "Nei") to Code("2", "Nei"),
                )

                Then("the result should be the expected CodeList") {
                    result shouldBe expected
                }
            }
        }
    })