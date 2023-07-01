package no.ssb.kostra.validation.rule

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

object TestUtils {
    fun verifyValidationResult(
        validationReportEntries: Collection<ValidationReportEntry>?,
        expectError: Boolean,
        expectedSeverity: Severity,
        expectedErrorText: String
    ) {
        if (expectError) {
            validationReportEntries.shouldNotBeNull()
            validationReportEntries.size.shouldBe(1)

            assertSoftly(validationReportEntries.first()) {
                severity.shouldBe(expectedSeverity)
                messageText.shouldBe(expectedErrorText)
            }
        } else validationReportEntries.shouldBeNull()
    }
}