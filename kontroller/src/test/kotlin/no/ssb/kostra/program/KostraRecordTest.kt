package no.ssb.kostra.program

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.extension.plus
import java.time.LocalDate

class KostraRecordTest : BehaviorSpec({
    Given("That data is supplied by default values") {
        val field = "field"
        val expectedMap = emptyMap<String, String>()

        When("data is based on an empty record string") {
            val kostraRecord = KostraRecord()

            Then("valuesByName shouldBe '$expectedMap'") {
                kostraRecord.valuesByName shouldBe expectedMap
            }

            Then("getFieldAsString(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.getFieldAsString(field)
                }
            }

            Then("getFieldAsTrimmedString(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.getFieldAsTrimmedString(field)
                }
            }

            Then("getFieldAsInteger(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.getFieldAsInteger(field)
                }
            }

            Then("getFieldAsIntegerDefaultEquals0(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.getFieldAsIntegerOrDefault(field)
                }
            }
        }
    }

    Given("that dataType is string") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "ddMMyyyy", false)
        ).associateBy { it.name }

        forAll(
            row("no data", mapOf("Field" to " ".repeat(3)), "   ", ""),
            row("all fields filled", mapOf("Field" to "AB "), "AB ", "AB")
        ) { description, valuesByName, string, trimmedString ->
            When(description) {
                val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

                Then("getFieldAsString") {
                    kostraRecord.getFieldAsString("Field") shouldBe string
                }

                Then("getFieldAsTrimmedString") {
                    kostraRecord.getFieldAsTrimmedString("Field") shouldBe trimmedString
                }
            }
        }
    }

    Given("that dataType is integer") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", INTEGER_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "ddMMyyyy", false)
        ).associateBy { it.name }

        forAll(
            row(
                "no data",
                mapOf("Field" to " ".repeat(3)),
                null,
                0
            ),
            row(
                "all fields filled",
                mapOf("Field" to "  1"),
                1,
                1
            )
        ) { description, valuesByName, intValue, defaultIntValue ->
            When(description) {
                val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

                Then("getFieldAsInteger") {
                    kostraRecord.getFieldAsInteger("Field") shouldBe intValue
                }

                Then("getFieldAsIntegerDefaultEquals0") {
                    kostraRecord.getFieldAsIntegerOrDefault("Field") shouldBe defaultIntValue
                }
            }
        }
    }

    Given("that dataType is date and using default datePattern of ddMMyyyy") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", DATE_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "", false)
        ).associateBy { it.name }

        forAll(
            row("no data", mapOf("Field" to " ".repeat(8)), null),
            row("all fields filled", mapOf("Field" to "24122023"), LocalDate.of(2023, 12, 24))
        ) { description, valuesByName, dateValue ->
            When(description) {
                val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

                Then("getFieldAsLocalDate") {
                    kostraRecord.getFieldAsLocalDate("Field") shouldBe dateValue
                }
            }
        }
    }

    Given("that dataType is date and using wrong datePattern of dd-MM-yyyy") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", DATE_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "dd-MM-yyyy", false)
        ).associateBy { it.name }

        forAll(
            row("no data", mapOf("Field" to " ".repeat(8)), null),
            row("all fields filled", mapOf("Field" to "24122023"), LocalDate.of(2023, 12, 24))
        ) { description, valuesByName, dateValue ->
            When(description) {
                val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

                Then("getFieldAsLocalDate") {
                    shouldThrow<IndexOutOfBoundsException> {
                        kostraRecord.getFieldAsLocalDate("Field") shouldBe dateValue
                    }
                }
            }
        }
    }

    Given("that fieldDefinitionsByName is set, but is missing a particular field") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", DATE_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "        ", false)
        ).associateBy { it.name }

        val valuesByName = mapOf("Field" to " ".repeat(8))

        When("Field is missing") {
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

            Then("getFieldDefinitionByName should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.getFieldDefinitionByName("MissingField")
                }
            }
        }
    }

    Given("testing of overridden function equals()") {
        val fieldDefinitions = listOf(
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "", false)
        )
        val valuesByName = mapOf("Field123" to "12", "Field456" to "456", "Field789" to "789", "Field012" to "-1")
        val recordString = "12 456789 -1"
        val kostraRecord1a = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })
        val kostraRecord1b = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })
        val kostraRecord2 = kostraRecord1a.plus("Added" to "be different")

        forAll(
            row("instances are equal", kostraRecord1a, kostraRecord1b, true),
            row("other instance is missing", kostraRecord1a, null, false),
            row("instances are different classes", kostraRecord1a, recordString, false),
            row("instances have different content", kostraRecord1a, kostraRecord2, false),
        ) { description, thisInstance, otherInstance, expectedResult ->
            When(description) {
                val testResult = thisInstance == otherInstance

                Then("result should be as expected") {
                    testResult shouldBe expectedResult
                }
            }
        }
    }

    Given("testing of overridden function toString()") {
        val fieldDefinitions = listOf(
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "", false)
        )
        val valuesByName = mapOf("Field123" to "123")
        val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })
        val result =
            "{Field123=123}\n{Field123=FieldDefinition(number=1, name=Field123, dataType=String, viewType=inputbox, from=1, to=3, codeList=[], datePattern=, mandatory=false)}"
        forAll(
            row("happy path scenario is created", kostraRecord, result),
        ) { description, thisInstance, expectedResult ->
            When(description) {
                val testResult = thisInstance.toString()

                Then("result should be as expected") {
                    testResult shouldBe expectedResult
                }
            }
        }
    }

    Given("testing of overridden function hashCode()") {
        val fieldDefinitions = listOf(
            FieldDefinition(1, "Field123", STRING_TYPE, INPUTBOX_VIEWTYPE, 1, 3, emptyList(), "", false)
        )
        val valuesByName = mapOf("Field123" to "123")
        val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })
        val result = -864379669
        forAll(
            row("happy path scenario is created", kostraRecord, result),
        ) { description, thisInstance, expectedResult ->
            When(description) {
                val testResult = thisInstance.hashCode()

                Then("result should be as expected") {
                    testResult shouldBe expectedResult
                }
            }
        }
    }
})