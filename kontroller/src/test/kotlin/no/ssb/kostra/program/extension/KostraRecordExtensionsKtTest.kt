package no.ssb.kostra.program.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.*
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class KostraRecordExtensionsKtTest : BehaviorSpec({

    Given("KostraRecord#fieldAs") {
        val sutWithValue = KostraRecord(
            valuesByName = mapOf(
                "Field" to "42 ",
            ),
            fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
        )
        val sutWithoutValue = sutWithValue.copy(valuesByName = mapOf(
            "Field" to " ",
        ))

        When("fieldAs<Int>") {
            sutWithValue.fieldAs<Int>("Field").shouldBe(42)
        }
        When("fieldAs<Int?>") {
            sutWithoutValue.fieldAs<Int?>("Field").shouldBe(null)
        }

        When("fieldAs<String>") {
            sutWithValue.fieldAs<String>("Field").shouldBe("42")
        }
        When("fieldAs<String>, trim=false") {
            sutWithValue.fieldAs<String>("Field", false).shouldBe("42 ")
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
            "Field123" to "12",
            "Field456" to "456",
            "Field789" to "789",
            "Field012" to "-1"
        ).toKostraRecord()

        When("plus") {
            val changedByPlus = sut.plus("Added" to "911")

            Then("the added should have the provided value") {
                changedByPlus.fieldAsString("Added") shouldBe "911"
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
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "", false),
            FieldDefinition(2, "Field456", STRING_TYPE, INPUTBOX_VIEWTYPE, 4, 6, emptyList(), "", false),
            FieldDefinition(3, "Field789", STRING_TYPE, INPUTBOX_VIEWTYPE, 7, 9, emptyList(), "", false),
            FieldDefinition(4, "Field012", INTEGER_TYPE, INPUTBOX_VIEWTYPE, 10, 12, emptyList(), "", false)
        )
    }
}
