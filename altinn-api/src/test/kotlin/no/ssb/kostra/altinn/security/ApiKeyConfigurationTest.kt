package no.ssb.kostra.altinn.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ApiKeyConfigurationTest : StringSpec({
    "should return empty apiKeys when allowed is blank" {
        val config = ApiKeyConfiguration().apply { allowed = "" }
        config.apiKeys() shouldBe emptySet()
        config.isSecurityDisabled() shouldBe true
    }

    "should split allowed string into apiKeys set" {
        val config = ApiKeyConfiguration().apply { allowed = "key1, key2 ,key3" }
        config.apiKeys() shouldBe setOf("key1", "key2", "key3")
        config.isSecurityDisabled() shouldBe false
    }

    "should filter out blank and trim keys" {
        val config = ApiKeyConfiguration().apply { allowed = " key1 , , key2 ,, " }
        config.apiKeys() shouldBe setOf("key1", "key2")
    }

    "isValid should return true for valid key" {
        val config = ApiKeyConfiguration().apply { allowed = "key1,key2" }
        config.isValid("key1") shouldBe true
        config.isValid("key2") shouldBe true
    }

    "isValid should return false for invalid or null key" {
        val config = ApiKeyConfiguration().apply { allowed = "key1,key2" }
        config.isValid("key3") shouldBe false
        config.isValid(null) shouldBe false
    }

    "isSecurityDisabled should be true if no keys" {
        val config = ApiKeyConfiguration().apply { allowed = "   , ,  " }
        config.isSecurityDisabled() shouldBe true
    }
})

