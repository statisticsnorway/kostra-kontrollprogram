package no.ssb.kostra.validation.rule

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

object TestUtils {
    fun verifyValidationResult(
        validationReportEntries: Collection<ValidationReportEntry>?,
        expectError: Boolean,
        expectedSeverity: Severity,
        expectedErrorText: String,
        expectedSize: Int = 1
    ) {
        if (expectError) {
            validationReportEntries.shouldNotBeNull()
            validationReportEntries.size.shouldBe(expectedSize)

            assertSoftly(validationReportEntries.first()) {
                severity.shouldBe(expectedSeverity)
                messageText.shouldStartWith(expectedErrorText)
            }
        } else validationReportEntries.shouldBeNull()
    }
}