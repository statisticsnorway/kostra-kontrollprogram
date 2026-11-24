package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.DataType.*
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord

class Rule002FileDescriptionTest : BehaviorSpec({
    data class Case(
        val description: String,
        val fieldDefinition: FieldDefinition,
        val recordString: String
    )

    val templateFieldDefinition =
        FieldDefinition(
            name = "someFieldName",
            dataType = STRING_TYPE,
            from = 1,
            size = 5
        )
    val kotlinArguments = KotlinArguments(skjema = "SS", aargang = "YYYY", region = "RRRRRR")

    Given("context and field definitions, results in null") {
        listOf(
            Case(
                "Optional string, no content",
                templateFieldDefinition,
                "     ",
            ),
            Case(
                "Mandatory string, has content",
                templateFieldDefinition.copy(mandatory = true),
                "OK   ",
            ),
            Case(
                "Optional string, has code list, no content",
                templateFieldDefinition.copy(codeList = listOf(Code("ABCDE", "ABCDE"))),
                "     ",
            ),
            Case(
                "Mandatory string, has code list, correct content",
                templateFieldDefinition.copy(codeList = listOf(Code("ABCDE", "ABCDE")), mandatory = true),
                "ABCDE",
            ),
            Case(
                "Optional integer, no content",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "     ",
            ),
            Case(
                "Optional integer, correct content",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "    1",
            ),
            Case(
                "Optional date, no content",
                templateFieldDefinition.copy(dataType = DATE_TYPE, from = 1, size = 8, datePattern = "ddMMyyyy"),
                " ".repeat(8),
            ),
            Case(
                "Field is date, correct content",
                templateFieldDefinition.copy(dataType = DATE_TYPE, from = 1, size = 8, datePattern = "ddMMyyyy"),
                "01012023",
            ),

        ).forEach { (description, fieldDefinition, recordString) ->
            When(description) {
                val sut = Rule002FileDescription(listOf(fieldDefinition))
                val result = sut.validate(
                    context = listOf(recordString)
                        .withIndex()
                        .map { (index, recordString) ->
                            recordString.toKostraRecord(index = index, fieldDefinitions = listOf(fieldDefinition))
                        },
                    arguments = kotlinArguments
                )

                Then("result should be null") {
                    result.shouldBeNull()
                }
            }
        }
    }

    Given("context and field definitions, results in validation report entry") {
        listOf(
            Case(
                "Mandatory string, no content",
                templateFieldDefinition.copy(mandatory = true),
                "     ",
            ),
            Case(
                "Field has code list, but incorrect content",
                templateFieldDefinition.copy(codeList = listOf(Code("ABCDE", "~Title~"))),
                "FAIL!",
            ),
            Case(
                "Field is integer, but incorrect content",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "FAIL!",
            ),
            Case(
                "Field is integer, but incorrect content. Defined as Float with comma as separator",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "  3,5",
            ),
            Case(
                "Field is integer, but incorrect content. Defined as Float with period as separator",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "  3.5",
            ),
            Case(
                "Field is date, but incorrect content",
                templateFieldDefinition.copy(dataType = DATE_TYPE, from = 1, size = 8, datePattern = "ddMMyyyy"),
                "BIG FAIL",
            ),

        ).forEach { (description, fieldDefinition, recordString) ->
            When(description) {
                val sut = Rule002FileDescription(listOf(fieldDefinition))
                val result = sut.validate(
                    context = listOf(recordString)
                        .withIndex()
                        .map { (index, recordString) ->
                            recordString.toKostraRecord(index = index, fieldDefinitions = listOf(fieldDefinition))
                        },
                    arguments = kotlinArguments
                )

                Then("result should not be null") {
                    result.shouldNotBeNull()
                    result.size shouldBe 1
                }
            }
        }
    }
})