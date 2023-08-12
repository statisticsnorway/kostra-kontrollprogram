package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.*

class KostraRecordExtensionsKtTest : BehaviorSpec({

    Given("KostraRecord#toRecordString") {
        forAll(
            row(
                "empty fieldDefinitions",
                emptyMap(),
                ""
            ),
            row(
                "non-empty fieldDefinitions",
                fieldDefinitions.associateBy { it.name },
                "12 456789 -1"
            )
        ) { description, fieldDefinitions, expectedRecordString ->
            val sut = KostraRecord(
                valuesByName = mapOf(
                    "Field001" to "",
                    "Field012" to " ".repeat(3),
                    "Field123" to "12",
                    "Field456" to "456",
                    "Field789" to "789",
                    "Field012" to "-1"
                ),
                fieldDefinitionByName = fieldDefinitions
            )

            When("toRecordString, $description") {
                val recordString = sut.toRecordString()

                Then("the result should be a concatenated string of all the values") {
                    recordString shouldBe expectedRecordString
                }
            }
        }
    }

    Given("KostraRecord#plus") {
        val sut = mapOf(
            "Field123" to "12 ",
            "Field456" to "456",
            "Field789" to "789",
            "Field012" to " -1"
        ).toKostraRecord(1, fieldDefinitions)

        When("plus") {
            val changedByPlus = sut.plus("Added" to "911")

            Then("the added should have the provided value") {
                changedByPlus["Added"] shouldBe "911"
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
                kostraRecord.lineNumber shouldBe 0
            }
        }
    }

    Given("KostraRecord#asList") {
        val sut = "12 456789 -1"

        When("toKostraRecord") {
            val kostraRecord = sut.toKostraRecord(0, fieldDefinitions)

            Then("the result should be a concatenated string of all the values") {
                kostraRecord.asList() shouldBe listOf(kostraRecord)
            }
        }
    }

    Given("KostraRecord#Map#toKostraRecord") {
        val sut = mapOf(
            "Field123" to "12 ",
            "Field456" to "456",
            "Field789" to "789",
            "Field012" to " -1"
        )

        When("toKostraRecord") {
            val kostraRecord = sut.toKostraRecord(0, fieldDefinitions)

            Then("the result should be a concatenated string of all the values") {
                kostraRecord.valuesByName shouldBe mapOf(
                    "Field123" to "12 ",
                    "Field456" to "456",
                    "Field789" to "789",
                    "Field012" to " -1"
                )
                kostraRecord.lineNumber shouldBe 0
            }
        }
    }

}) {
    companion object {
        private val fieldDefinitions = listOf(
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "", false),
            FieldDefinition(2, "Field456", STRING_TYPE, INPUTBOX_VIEWTYPE, 4, 6, emptyList(), "", false),
            FieldDefinition(3, "Field789", STRING_TYPE, INPUTBOX_VIEWTYPE, 7, 9, emptyList(), "", false),
            FieldDefinition(4, "Field012", INTEGER_TYPE, INPUTBOX_VIEWTYPE, 10, 12, emptyList(), "", false)
        )
    }
}
