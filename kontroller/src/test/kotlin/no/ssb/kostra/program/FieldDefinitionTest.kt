package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe


class FieldDefinitionTest : BehaviorSpec({
    Given("datePattern context") {
        forAll(
            row("default", "", DATE8_PATTERN),
            row("blank", "        ", DATE8_PATTERN),
            row("DATE8_PATTERN", DATE8_PATTERN, DATE8_PATTERN),

            ){ description, datePattern, expectedResultDatePattern ->
            When(description) {
                val result = FieldDefinition(
                    dataType = DATE_TYPE,
                    datePattern = datePattern
                )

                Then("${result.datePattern} should be $expectedResultDatePattern") {
                    result.datePattern shouldBe expectedResultDatePattern
                }
            }
        }
    }

    Given("viewType context") {
        forAll(
            row("default for $INPUTBOX_VIEWTYPE", INPUTBOX_VIEWTYPE, listOf(), 0),
            row("default for $CHECKBOX_VIEWTYPE", CHECKBOX_VIEWTYPE, listOf(), 3),
            row("CodeList with 2 items for $CHECKBOX_VIEWTYPE", CHECKBOX_VIEWTYPE, listOf(Code("1", "1"), Code("2", "2")), 2),

            ){ description, viewType, codeList, expectedResultCodeListLength ->
            When(description) {
                val result = FieldDefinition(
                    viewType = viewType,
                    codeList = codeList
                )

                Then("codeList.size should be $expectedResultCodeListLength") {
                    result.codeList.size shouldBe expectedResultCodeListLength
                }
            }
        }
    }
})