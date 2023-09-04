package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class CodeTest : BehaviorSpec({
    Given("context") {
        val sut = Code(
            code = "1",
            value = "Value of code 1"
        )

        When("running toString()") {
            val result = sut.toString()

            Then("result should formatted as expected") {
                result shouldBe "1=Value of code 1"
            }
        }
    }
})