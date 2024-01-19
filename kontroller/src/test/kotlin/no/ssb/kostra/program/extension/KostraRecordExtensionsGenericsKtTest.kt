package no.ssb.kostra.program.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.DataType.*
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord
import java.time.LocalDate

class KostraRecordExtensionsGenericsKtTest : BehaviorSpec({

    Given("KostraRecord#fieldAs") {
        val sutWithValue = KostraRecord(
            valuesByName = mapOf(STRING_FIELD to "ABC "),
            fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
        )
        val sutWithoutValue = sutWithValue.copy(valuesByName = mapOf(STRING_FIELD to "    "))
        val sutWithIntegerValue = sutWithValue.copy(valuesByName = mapOf(INTEGER_FIELD to "42 "))
        val sutWithoutIntegerValue = sutWithValue.copy(valuesByName = mapOf(INTEGER_FIELD to " "))
        val sutWithDateValue = sutWithValue.copy(valuesByName = mapOf(DATE_FIELD to "01012023"))
        val sutWithoutDateValue = sutWithValue.copy(valuesByName = mapOf(DATE_FIELD to " ".repeat(8)))
        val sutWithIllegalDateValue = sutWithValue.copy(valuesByName = mapOf(DATE_FIELD to "32122023"))


        When("fieldAs<String>") {
            sutWithValue.fieldAs<String>(STRING_FIELD).shouldBe("ABC")
        }

        When("fieldAs<String?>") {
            sutWithoutValue.fieldAs<String?>(STRING_FIELD).shouldBe(null)
        }

        When("fieldAs<Int>") {
            sutWithIntegerValue.fieldAs<Int>(INTEGER_FIELD).shouldBe(42)
        }

        When("fieldAs<Int?>") {
            sutWithoutIntegerValue.fieldAs<Int?>(INTEGER_FIELD).shouldBe(null)
        }

        When("fieldAs<LocalDate>") {
            sutWithDateValue.fieldAs<LocalDate>(DATE_FIELD).shouldBe(LocalDate.of(2023, 1, 1))
        }

        When("fieldAs<LocalDate?>") {
            sutWithoutDateValue.fieldAs<LocalDate?>(DATE_FIELD).shouldBe(null)
        }

        When("fieldAs<LocalDate?> with illegal value") {
            sutWithIllegalDateValue.fieldAs<LocalDate?>(DATE_FIELD).shouldBe(null)
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
}) {
    companion object {
        private const val STRING_FIELD = "STRING_FIELD"
        private const val INTEGER_FIELD = "INTEGER_FIELD"
        private const val DATE_FIELD = "DATE_FIELD"
        val fieldDefinitions: List<FieldDefinition> = listOf(
            FieldDefinition(
                name = STRING_FIELD,
                dataType = STRING_TYPE,
                size = 4
            ),
            FieldDefinition(
                name = INTEGER_FIELD,
                dataType = INTEGER_TYPE,
                size = 2
            ),
            FieldDefinition(
                name = DATE_FIELD,
                dataType = DATE_TYPE,
                size = 8,
                datePattern = "ddMMyyyy"
            )
        )
    }
}
