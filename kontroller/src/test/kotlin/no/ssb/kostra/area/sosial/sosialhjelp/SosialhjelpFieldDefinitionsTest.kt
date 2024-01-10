package no.ssb.kostra.area.sosial.sosialhjelp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SosialhjelpFieldDefinitionsTest : BehaviorSpec({

    Given("SosialhjelpFieldDefinitions") {
        val sut = SosialhjelpFieldDefinitions.fieldDefinitions

        Then("records should be as expected") {
            sut.size shouldBe 78
        }
    }
})