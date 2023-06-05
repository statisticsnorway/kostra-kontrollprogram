package no.ssb.kostra.barn

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.BarnevernTestData.barnevernTypeInTest
import no.ssb.kostra.barn.KostraValidationUtils.validate
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance

class KostraValidationUtilsTest : BehaviorSpec({

    Given("validate") {

        val xmlToValidate = marshallInstance(barnevernTypeInTest)

        When("validate") {
            val isValid = validate(xmlToValidate)

            Then("isValid should be true") {
                isValid shouldBe true
            }
        }
    }
})