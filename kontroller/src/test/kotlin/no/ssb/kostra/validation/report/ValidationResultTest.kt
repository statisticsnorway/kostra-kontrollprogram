package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class ValidationResultTest : BehaviorSpec({
    Given("ValidationResult#severity & numberOfControls") {
        forAll(
            row(
                "numberOfControls = 0, no entries",
                0,
                emptyList(),
                Severity.ERROR,
                0,
                0,
            ),
            row(
                "numberOfControls = 1, 2 unique entries",
                1,
                listOf(
                    ValidationReportEntry().copy(
                        ruleName = "Rule 1",
                        severity = Severity.WARNING,
                        lineNumbers = listOf(1)
                    ),
                    ValidationReportEntry().copy(
                        ruleName = "Rule 2",
                        severity = Severity.INFO,
                        lineNumbers = listOf(2)
                    ),
                ),
                Severity.WARNING,
                2,
                2,
            ),
            row(
                "numberOfControls = 1, 2 duplicate entries",
                1,
                listOf(
                    ValidationReportEntry().copy(
                        ruleName = "Rule 1",
                        severity = Severity.WARNING,
                        lineNumbers = listOf(1)
                    ),
                    ValidationReportEntry().copy(
                        ruleName = "Rule 1",
                        severity = Severity.WARNING,
                        lineNumbers = listOf(2)
                    ),
                ),
                Severity.WARNING,
                2,
                1,
            ),
            row(
                "numberOfControls = 1, no entries",
                1,
                emptyList(),
                Severity.OK,
                0,
                0,
            ),
        ) { description, numberOfControls, reportEntries, expectedSeverity, expectedCount, expectedUnique ->
            When(description) {
                val sut = ValidationResult(
                    reportEntries = reportEntries,
                    numberOfControls = numberOfControls,
                )

                Then("result should be as expected") {
                    sut.severity shouldBe expectedSeverity
                    sut.count shouldBe expectedCount
                    sut.uniqueReportEntries.size shouldBe expectedUnique
                }
            }
        }
    }
})