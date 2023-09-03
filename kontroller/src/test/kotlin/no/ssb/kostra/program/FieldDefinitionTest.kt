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
            row("DATE8_PATTERN", DATE8_PATTERN, DATE8_PATTERN)
        ) { description, datePattern, expectedResultDatePattern ->
            When(description) {
                val result = FieldDefinition(
                    name = "~name~",
                    dataType = DATE_TYPE,
                    datePattern = datePattern
                )

                Then("${result.datePattern} should be $expectedResultDatePattern") {
                    result.datePattern shouldBe expectedResultDatePattern
                    result.length shouldBe 1
                }
            }
        }
    }
})