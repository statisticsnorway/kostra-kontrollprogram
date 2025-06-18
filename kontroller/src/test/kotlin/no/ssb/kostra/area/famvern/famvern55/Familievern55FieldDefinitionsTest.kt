package no.ssb.kostra.area.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class Familievern55FieldDefinitionsTest : BehaviorSpec({
    Given("Familievern55FieldDefinitions") {
        When("sut") {
            Then("length is as expected") {
                Familievern55FieldDefinitions.fieldLength shouldBe 659
            }
        }
    }
})