package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.DataType.*
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KotlinArguments

class Rule002FileDescriptionTest : BehaviorSpec({
    val templateFieldDefinition =
        FieldDefinition(
            name = "someFieldName",
            dataType = STRING_TYPE,
            from = 1,
            size = 5
        )
    val kotlinArguments = KotlinArguments(skjema = "SS", aargang = "YYYY", region = "RRRRRR")

    Given("context and field definitions, results in null") {
        forAll(
            row(
                "Optional string, no content",
                templateFieldDefinition,
                "     ",
            ),
            row(
                "Mandatory string, has content",
                templateFieldDefinition.copy(mandatory = true),
                "OK   ",
            ),
            row(
                "Optional string, has code list, no content",
                templateFieldDefinition.copy(codeList = listOf(Code("ABCDE", "ABCDE"))),
                "     ",
            ),
            row(
                "Mandatory string, has code list, correct content",
                templateFieldDefinition.copy(codeList = listOf(Code("ABCDE", "ABCDE")), mandatory = true),
                "ABCDE",
            ),
            row(
                "Optional integer, no content",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "     ",
            ),
            row(
                "Optional integer, correct content",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "    1",
            ),
            row(
                "Optional date, no content",
                templateFieldDefinition.copy(dataType = DATE_TYPE, from = 1, size = 8, datePattern = "ddMMyyyy"),
                " ".repeat(8),
            ),
            row(
                "Field is date, correct content",
                templateFieldDefinition.copy(dataType = DATE_TYPE, from = 1, size = 8, datePattern = "ddMMyyyy"),
                "01012023",
            ),

            ) { description, fieldDefinition, recordString ->
            When(description) {
                val sut = Rule002FileDescription(listOf(fieldDefinition))
                val result = sut.validate(
                    context = listOf(recordString),
                    arguments = kotlinArguments
                )

                Then("result should be null") {
                    result.shouldBeNull()
                }
            }
        }
    }

    Given("context and field definitions, results in validation report entry") {
        forAll(
            row(
                "Mandatory string, no content",
                templateFieldDefinition.copy(mandatory = true),
                "     ",
            ),
            row(
                "Field has code list, but incorrect content",
                templateFieldDefinition.copy(codeList = listOf(Code("ABCDE", "~Title~"))),
                "FAIL!",
            ),
            row(
                "Field is integer, but incorrect content",
                templateFieldDefinition.copy(dataType = INTEGER_TYPE),
                "FAIL!",
            ),
            row(
                "Field is date, but incorrect content",
                templateFieldDefinition.copy(dataType = DATE_TYPE, from = 1, size = 8, datePattern = "ddMMyyyy"),
                "BIG FAIL",
            ),

            ) { description, fieldDefinition, recordString ->
            When(description) {
                val sut = Rule002FileDescription(listOf(fieldDefinition))
                val result = sut.validate(
                    context = listOf(recordString),
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