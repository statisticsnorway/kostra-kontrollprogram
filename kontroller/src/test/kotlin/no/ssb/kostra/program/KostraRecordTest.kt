package no.ssb.kostra.program

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.extension.fieldAs
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

            Then("fieldAsString(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.fieldAsString(field)
                }
            }

            Then("fieldAsTrimmedString(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.fieldAsTrimmedString(field)
                }
            }

            Then("fieldAsInt(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.fieldAsInt(field)
                }
            }

            Then("fieldAsIntOrDefault(\"$field\") should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.fieldAsIntOrDefault(field)
                }
            }
        }
    }

    Given("that dataType is string") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", STRING_TYPE, 1, 3, emptyList(), "ddMMyyyy", false)
        ).associateBy { it.name }

        forAll(
            row(
                "no data",
                mapOf("Field" to " ".repeat(3)),
                "   ",
                "",
                null
            ),
            row(
                "all fields filled",
                mapOf("Field" to "AB "),
                "AB ",
                "AB",
                "AB"
            ),
            row(
                "field with 0",
                mapOf("Field" to "0 "),
                "0 ",
                "0",
                null
            )
        ) { description, valuesByName, string, trimmedString, trimmedToNull ->
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

            When("$description, fieldAsString") {
                kostraRecord.fieldAsString("Field").shouldBe(string)
            }

            And("$description, fieldAsTrimmedString") {
                kostraRecord.fieldAsTrimmedString("Field").shouldBe(trimmedString)
            }

            And("$description, get") {
                kostraRecord["Field"].shouldBe(string)
            }

            And("$description, fieldAs<String>") {
                kostraRecord.fieldAs<String>("Field").shouldBe(trimmedString)
            }

            And("$description, fieldAs<String?>") {
                kostraRecord.fieldAs<String?>("Field").shouldBe(trimmedToNull)
            }
        }
    }

    Given("that dataType is integer") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", INTEGER_TYPE, 1, 3, emptyList(), "ddMMyyyy", false)
        ).associateBy { it.name }

        forAll(
            row(
                "no data",
                mapOf("Field" to " ".repeat(3)),
                null, 0
            ),
            row(
                "all fields filled",
                mapOf("Field" to "  1"),
                1, 1
            )
        ) { description, valuesByName, intValue, defaultIntValue ->
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

            When("$description, fieldAsInt") {
                kostraRecord.fieldAsInt("Field").shouldBe(intValue)
            }

            And("$description, fieldAsIntOrDefault") {
                kostraRecord.fieldAsIntOrDefault("Field") shouldBe defaultIntValue
            }

            And("$description, fieldAs<Int?>") {
                kostraRecord.fieldAs<Int?>("Field").shouldBe(intValue)
            }

            And("$description, fieldAs<Int>") {
                kostraRecord.fieldAs<Int>("Field").shouldBe(defaultIntValue)
            }
        }
    }

    Given("that dataType is date and using default datePattern of ddMMyyyy") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", DATE_TYPE, 1, 3, emptyList(), "", false)
        ).associateBy { it.name }

        forAll(
            row(
                "no data",
                mapOf("Field" to " ".repeat(8)),
                null
            ),
            row(
                "all fields filled",
                mapOf("Field" to "24122023"),
                LocalDate.of(2023, 12, 24)
            )
        ) { description, valuesByName, dateValue ->
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

            When("$description fieldAsLocalDate") {
                kostraRecord.fieldAsLocalDate("Field") shouldBe dateValue
            }

            And("$description, fieldAs<LocalDate?>") {
                kostraRecord.fieldAs<LocalDate?>("Field").shouldBe(dateValue)
            }

            And("$description, fieldAs<LocalDate>") {
                if (dateValue == null) {
                    shouldThrow<NullPointerException> {
                        kostraRecord.fieldAs<LocalDate>("Field")
                    }
                } else {
                    kostraRecord.fieldAs<LocalDate>("Field").shouldBe(dateValue)
                }
            }
        }
    }

    Given("that dataType is date and using wrong datePattern of dd-MM-yyyy") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", DATE_TYPE, 1, 3, emptyList(), "dd-MM-yyyy", false)
        ).associateBy { it.name }

        forAll(
            row(
                "no data",
                mapOf("Field" to " ".repeat(8)),
                null
            ),
            row(
                "all fields filled",
                mapOf("Field" to "24122023"),
                LocalDate.of(2023, 12, 24)
            )
        ) { description, valuesByName, dateValue ->
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

            When(description) {
                shouldThrow<IndexOutOfBoundsException> {
                    kostraRecord.fieldAsLocalDate("Field") shouldBe dateValue
                }
            }
        }
    }

    Given("that fieldDefinitionsByName is set, but is missing a particular field") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(0, "Field", DATE_TYPE, 1, 3, emptyList(), "        ", false)
        ).associateBy { it.name }

        val valuesByName = mapOf("Field" to " ".repeat(8))

        When("Field is missing") {
            val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitionsByName)

            Then("getFieldDefinitionByName should throw NoSuchFieldException") {
                shouldThrow<NoSuchFieldException> {
                    kostraRecord.fieldDefinition("MissingField")
                }
            }
        }
    }

    Given("testing of overridden function equals()") {
        val kostraRecord1a = KostraRecord()
        val kostraRecord1b = KostraRecord()
        val kostraRecord2 = kostraRecord1a.plus("Added" to "be different")

        forAll(
            row("instances are equal", kostraRecord1a, kostraRecord1a, true),
            row("instances are context equal", kostraRecord1a, kostraRecord1b, true),
            row("other instance is null", kostraRecord1a, null, false),
            row("other is of different type", kostraRecord1a, "Hello World", false),
            row("instances have different lineNumber", kostraRecord1a, kostraRecord1a.copy(lineNumber = 2), false),
            row("instances have different valuesByName", kostraRecord1a, kostraRecord2, false),
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
            FieldDefinition(1, "Field123", STRING_TYPE, 1, 3, emptyList(), "", false)
        )
        val valuesByName = mapOf("Field123" to "123")
        val kostraRecord = KostraRecord(0, valuesByName, fieldDefinitions.associateBy { it.name })
        val result =
            "{Field123=123}\n{Field123=FieldDefinition(number=1, name=Field123, dataType=String, from=1, to=3, codeList=[], datePattern=, mandatory=false, size=1)}"
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
            FieldDefinition(1, "Field123", STRING_TYPE, 1, 3, emptyList(), "", false)
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