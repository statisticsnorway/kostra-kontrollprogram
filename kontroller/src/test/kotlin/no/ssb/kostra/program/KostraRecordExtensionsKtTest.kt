package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class KostraRecordExtensionsKtTest : BehaviorSpec({

    Given("KostraRecord#toRecordString") {
        val sut = KostraRecord(
            index = 0,
            valuesByName = mapOf(
                "Field001" to "",
                "Field012" to " ".repeat(3),
                "Field123" to "12",
                "Field456" to "456",
                "Field789" to "789",
                "Field012" to "-1"
            ),
            fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
        )

        When("toRecordString") {
            val recordString = sut.toRecordString()

            Then("the result should be a concatenated string of all the values") {
                recordString shouldBe "12 456789 -1"
            }
        }
    }

    Given("KostraRecord#plus") {
        val sut = KostraRecord(
            index = 0,
            valuesByName = mapOf(
                "Field123" to "12",
                "Field456" to "456",
                "Field789" to "789",
                "Field012" to "-1"
            ),
            fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
        )

        When("plus") {
            val changedByPlus = sut.plus("Added" to "911")

            Then("the added should have the provided value") {
                changedByPlus.getFieldAsString("Added") shouldBe "911"
            }
        }
    }

    Given("KostraRecord#valuesByName") {
        val sut = "12 456789 -1"

        When("toKostraRecord") {
            val kostraRecord = sut.toKostraRecord(0, fieldDefinitions)

            Then("the result should be a concatenated string of all the values") {
                kostraRecord.valuesByName shouldBe mapOf(
                    "Field123" to "12 ",
                    "Field456" to "456",
                    "Field789" to "789",
                    "Field012" to " -1"
                )
            }
        }
    }
}) {
    companion object {
        private val fieldDefinitions = listOf(
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, listOf(), "", false),
            FieldDefinition(2, "Field456", STRING_TYPE, INPUTBOX_VIEWTYPE, 4, 6, listOf(), "", false),
            FieldDefinition(3, "Field789", STRING_TYPE, INPUTBOX_VIEWTYPE, 7, 9, listOf(), "", false),
            FieldDefinition(4, "Field012", INTEGER_TYPE, INPUTBOX_VIEWTYPE, 10, 12, listOf(), "", false)
        )
    }
}
