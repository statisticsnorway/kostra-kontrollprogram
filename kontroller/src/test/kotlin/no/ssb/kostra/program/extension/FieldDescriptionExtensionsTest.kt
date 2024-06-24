package no.ssb.kostra.program.extension

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.DataType

class FieldDescriptionExtensionsTest : BehaviorSpec({

    Given("FileDescription with test values") {
        val sut = "test".toFileDescription().fields.buildFieldDefinitions()

        When("FileDescription is created") {
            Then("result of buildFieldDefinitions() should be as expected") {
                assertSoftly(sut) {
                    first().name shouldBe "FIELD1"
                    first().size shouldBe 4
                    first().dataType shouldBe DataType.STRING_TYPE
                }
            }
        }
    }
})