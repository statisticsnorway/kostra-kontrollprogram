package no.ssb.kostra.validation.rule

import io.kotest.assertions.asClue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

object TestUtils {
    fun verifyValidationResult(
        validationReportEntries: Collection<ValidationReportEntry>?,
        numberOfExpectedErrors: Int,
        expectedSeverity: Severity,
        expectedErrorText: String,
        expectedContextId: String? = null,
    ) {
        if (numberOfExpectedErrors > 0) {

            "At least one error should be present when expected".asClue {
                validationReportEntries.shouldNotBeNull()

                "Number of actual errors should match the number of expected errors".asClue {
                    validationReportEntries.size.shouldBe(numberOfExpectedErrors)
                }

                ("There should be an entry with (contextId=${expectedContextId}) and message which starts with " +
                        "(\"${expectedErrorText}\") among the following entries: \n($validationReportEntries)").asClue {

                    val validationReportEntry = validationReportEntries.find {
                        it.contextId == expectedContextId
                                && it.messageText.startsWith(expectedErrorText)
                    }

                    validationReportEntry.shouldNotBeNull()

                    validationReportEntry.asClue {
                        it.severity.shouldBe(expectedSeverity)
                        it.contextId.shouldBe(expectedContextId)
                        it.messageText.shouldStartWith(expectedErrorText)
                    }
                }
            }
        } else {
            { "No error should be present when not expected" }.asClue {
                validationReportEntries.shouldBeNull()
            }
        }
    }

    fun verifyValidationResult(
        validationReportEntries: Collection<ValidationReportEntry>?,
        expectError: Boolean,
        expectedSeverity: Severity,
        expectedErrorText: String
    ) {
        verifyValidationResult(
            validationReportEntries,
            numberOfExpectedErrors = when (expectError) {
                false -> 0
                true -> 1
            },
            expectedSeverity = expectedSeverity,
            expectedErrorText = expectedErrorText,
            expectedContextId = ""
        )
    }
}