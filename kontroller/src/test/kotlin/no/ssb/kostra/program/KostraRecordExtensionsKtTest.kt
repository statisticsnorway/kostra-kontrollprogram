package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class KostraRecordExtensionsKtTest : BehaviorSpec({

    Given("testing of extension functions") {
        val fieldDefinitions = listOf(
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, listOf(), "", false),
            FieldDefinition(2, "Field456", STRING_TYPE, INPUTBOX_VIEWTYPE, 4, 6, listOf(), "", false),
            FieldDefinition(3, "Field789", STRING_TYPE, INPUTBOX_VIEWTYPE, 7, 9, listOf(), "", false),
            FieldDefinition(4, "Field012", INTEGER_TYPE, INPUTBOX_VIEWTYPE, 10, 12, listOf(), "", false)
        )

        When("testing kostraRecord.toRecordString()") {
            val valuesByName = mapOf("Field123" to "12", "Field456" to "456", "Field789" to "789", "Field012" to "-1")
            val recordString = "12 456789 -1"
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })

            Then("the result should be a concatenated string of all the values") {
                kostraRecord.toRecordString() shouldBe recordString
            }
        }

        When("testing kostraRecord.plus()") {
            val valuesByName = mapOf("Field123" to "12", "Field456" to "456", "Field789" to "789", "Field012" to "-1")
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })

            Then("the added should have the provided value") {
                val changedByPlus = kostraRecord.plus("Added" to "911")
                changedByPlus.getFieldAsString("Added") shouldBe "911"
            }
        }

        When("testing String.toKostraRecord()") {
            val valuesByName = mapOf("Field123" to "12 ", "Field456" to "456", "Field789" to "789", "Field012" to " -1")
            val recordString = "12 456789 -1"
            val kostraRecord = recordString.toKostraRecord(0, fieldDefinitions)

            Then("the result should be a concatenated string of all the values") {
                kostraRecord.valuesByName shouldBe valuesByName
            }
        }
    }
})
