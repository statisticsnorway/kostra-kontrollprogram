package no.ssb.kostra

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.time.LocalDate

data class MyNestedClass(
    val someValue: Int
)

data class MyGenericClass<T : Any>(
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    val innerValue: T
)

class GenericsJacksonTest : BehaviorSpec({

    Given("serialize/deserialize generic class") {

        forAll(
            row("Int", 42, "42"),
            row("Double", 0.0, "0.0"),
            row("String", "Hello world", "\"Hello world\""),
            row("LocalDate", dateInTest, "[\"java.time.LocalDate\",\"2023-04-22\"]"),
            row("Long", 9223372036854775807L, "[\"java.lang.Long\",9223372036854775807]"),
            row("Float", 2.7182817f, "[\"java.lang.Float\",2.7182817]"),
            row("MyNestedClass", MyNestedClass(42), createInnerJson()),
        ) { description, currentValue, jsonValue ->

            When("serialize $description") {
                val json = objectMapper.writeValueAsString(MyGenericClass(currentValue))

                Then("result should be as expected") {
                    json shouldBe createJson(jsonValue)
                }
            }

            When("deserialize $description") {
                val deserialized = objectMapper.readValue<MyGenericClass<Any>>(createJson(jsonValue))

                Then("result should be as expected") {
                    deserialized shouldBe MyGenericClass(innerValue = currentValue)
                }
            }
        }
    }
}) {
    companion object {

        private val dateInTest = LocalDate.of(2023, 4, 22)

        private val objectMapper = jacksonObjectMapper()
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(JavaTimeModule())


        private fun createInnerJson(): String {
            return "{\"@class\":\"no.ssb.kostra.MyNestedClass\",\"someValue\":42}"
        }

        private fun createJson(innerValue: String): String {
            return "{\"innerValue\":${innerValue}}"
        }
    }
}
