package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class Rule001RecordLengthTest : BehaviorSpec({

    Given("valid context") {
        val sut = Rule001RecordLength(OK_STRING.length)

        When("valid list of strings") {
            val result = sut.validate(listOf(OK_STRING, OK_STRING))

            Then("validation should pass with no errors") {
                result.shouldBeNull()
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "record length is valid, but it ends with tab char, expect error",
                2,
                listOf("O\t"),
                1,
                listOf(1)
            ),
            row(
                "Ok, Fail -> 1 Error @ line #2",
                OK_STRING.length,
                listOf(OK_STRING, FAIL_STRING),
                1,
                listOf(2)
            ),
            row(
                "Ok, Fail, Ok, Fail -> 2 Errors @ line #2, #4",
                OK_STRING.length,
                listOf(OK_STRING, FAIL_STRING, OK_STRING, FAIL_STRING),
                2,
                listOf(2, 4)
            ),
            row(
                "Ok, Fail, Ok, Tab -> 2 Errors @ line #2, #4",
                OK_STRING.length,
                listOf(OK_STRING, FAIL_STRING, OK_STRING, "TAB\t"),
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

                Then("errors should be at the expected lineNumbers") {
                    errors?.flatMap { it.lineNumbers } shouldBe lineNumberOfErrors
                }
            }
        }
    }
}) {
    companion object {
        private const val OK_STRING = "OK"
        private const val FAIL_STRING = "FAIL"
    }
}