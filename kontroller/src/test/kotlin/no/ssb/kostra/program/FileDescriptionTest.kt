package no.ssb.kostra.program

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FileDescriptionTest : BehaviorSpec({

    Given("FileDescription with default values") {
        val sut = FileDescription()

        When("FileDescription is created") {
            Then("FileDescription should be as expected") {
                assertSoftly(sut) {
                    title shouldBe "File description"
                    reportingYear shouldBe 0
                    description shouldBe "Default file description"
                    fields shouldBe emptyList()
                }
            }
        }
    }
})