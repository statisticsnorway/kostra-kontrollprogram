package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class Rule001RecordLengthTest : BehaviorSpec({
    Given("valid context") {
        When("valid list of strings") {
            val sut = Rule001RecordLength("OK".length)

            Then("validation should pass with no errors") {
                sut.validate(listOf("OK", "OK")).shouldBeNull()
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "Ok, Fail -> 1 Error @ line #2",
                2,
                listOf("OK", "FAIL"),
                1,
                listOf(2)
            ),
            row(
                "Ok, Fail, Ok, Fail -> 2 Errors @ line #2, #4",
                2,
                listOf("OK", "FAIL", "OK", "FAIL"),
                2,
                listOf(2, 4)
            ),
            row(
                "Ok, Fail, Ok, Tab -> 2 Errors @ line #2, #4",
                2,
                listOf("OK", "FAIL", "OK", "TAB\t"),
                2,
                listOf(2, 4)
            )
        ) { description, recordStringLength, recordStringList, expectedErrorCount, lineNumberOfErrors ->
            When(description) {
                val sut = Rule001RecordLength(recordStringLength)
                val errors = sut.validate(recordStringList)

                Then("number of errors should be as expected") {
                    errors?.size shouldBe expectedErrorCount
                }

                Then("errors should be at the expected linenumbers") {
                    errors?.flatMap { it.lineNumbers } shouldBe lineNumberOfErrors
                }
            }
        }
    }
})