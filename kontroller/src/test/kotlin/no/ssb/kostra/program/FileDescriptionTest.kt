package no.ssb.kostra.program

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.extension.toFileDescription

class FileDescriptionTest : BehaviorSpec({

    Given("FileDescription with default values") {
        val sut = "test".toFileDescription()

        When("FileDescription is created") {
            Then("FileDescription should be as expected") {
                assertSoftly(sut) {
                    title shouldBe "Filebeskrivelse"
                    fields.size shouldBe 7
                    fields[0].dataType shouldBe DataType.STRING_TYPE
                }
                println(sut)
            }
        }
    }

    Given("Non-existing fileDescription") {
        When("FileDescription is created") {
            Then("result should throw NullPointerException") {
                shouldThrow<NullPointerException> { "FAIL".toFileDescription() }
            }
        }
    }
})