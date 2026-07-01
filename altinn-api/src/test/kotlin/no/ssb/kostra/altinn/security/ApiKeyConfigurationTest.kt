package no.ssb.kostra.altinn.security

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ApiKeyConfigurationTest : BehaviorSpec({

    Given("ApiKeyConfiguration") {
        val apiKeyConfiguration = ApiKeyConfiguration()

        When("allowed contains whitespace and blank entries") {
            val config = apiKeyConfiguration.apply { allowed = " key1 , , key2 ,, " }

            Then("apiKeys should trim values and filter blanks") {
                config.apiKeys() shouldBe setOf("key1", "key2")
            }
        }

        When("checking valid keys") {
            val config = ApiKeyConfiguration().apply { allowed = "key1,key2" }

            Then("isValid should return true for existing keys") {
                config.isValid("key1") shouldBe true
                config.isValid("key2") shouldBe true
            }
        }

        When("checking invalid or null key") {
            val config = ApiKeyConfiguration().apply { allowed = "key1,key2" }

            Then("isValid should return false") {
                config.isValid("key3") shouldBe false
                config.isValid(null) shouldBe false
            }
        }
    }
})