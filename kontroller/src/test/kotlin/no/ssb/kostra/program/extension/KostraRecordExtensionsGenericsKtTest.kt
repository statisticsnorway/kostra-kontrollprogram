package no.ssb.kostra.program.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord

class KostraRecordExtensionsGenericsKtTest : BehaviorSpec({

    Given("KostraRecord#fieldAs") {
        val sutWithValue = KostraRecord(
            valuesByName = mapOf(
                "Field" to "42 ",
            ),
            fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
        )
        val sutWithoutValue = sutWithValue.copy(
            valuesByName = mapOf(
                "Field" to " ",
            )
        )

        When("fieldAs<Int>") {
            sutWithValue.fieldAs<Int>("Field").shouldBe(42)
        }
        When("fieldAs<Int?>") {
            sutWithoutValue.fieldAs<Int?>("Field").shouldBe(null)
        }

        When("fieldAs<String>") {
            sutWithValue.fieldAs<String>("Field").shouldBe("42")
        }
        When("fieldAs<String?>") {
            sutWithoutValue.fieldAs<String?>("Field").shouldBe(null)
        }

        When("fieldAs<Long>") {
            val thrown = shouldThrow<IllegalArgumentException> {
                sutWithValue.fieldAs<Long>("Field")
            }

            Then("the exception should have the expected message") {
                thrown.message.shouldBe("fieldAs(): Unsupported type class kotlin.Long")
            }
        }
    }
})
