package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ValidationResultTest :
    BehaviorSpec({
        Given("ValidationResult#severity & numberOfControls") {
            data class Case(
                val description: String,
                val numberOfControls: Int,
                val reportEntries: List<ValidationReportEntry>,
                val expectedSeverity: Severity,
                val expectedCount: Int,
                val expectedUnique: Int,
            )

            listOf(
                Case(
                    "numberOfControls = 0, no entries",
                    0,
                    emptyList(),
                    Severity.ERROR,
                    0,
                    0,
                ),
                Case(
                    "numberOfControls = 1, 1 entries",
                    1,
                    listOf(
                        ValidationReportEntry().copy(
                            ruleName = "Rule 1",
                            severity = Severity.ERROR,
                            lineNumbers = listOf(1),
                        ),
                    ),
                    Severity.ERROR,
                    1,
                    1,
                ),
                Case(
                    "numberOfControls = 1, 2 unique entries",
                    1,
                    listOf(
                        ValidationReportEntry().copy(
                            ruleName = "Rule 1",
                            severity = Severity.WARNING,
                            lineNumbers = listOf(1),
                        ),
                        ValidationReportEntry().copy(
                            ruleName = "Rule 2",
                            severity = Severity.INFO,
                            lineNumbers = listOf(2),
                        ),
                    ),
                    Severity.WARNING,
                    2,
                    2,
                ),
                Case(
                    "numberOfControls = 1, 2 duplicate entries",
                    1,
                    listOf(
                        ValidationReportEntry().copy(
                            ruleName = "Rule 1",
                            severity = Severity.WARNING,
                            lineNumbers = listOf(1),
                        ),
                        ValidationReportEntry().copy(
                            ruleName = "Rule 1",
                            severity = Severity.WARNING,
                            lineNumbers = listOf(2),
                        ),
                    ),
                    Severity.WARNING,
                    2,
                    1,
                ),
                Case(
                    "numberOfControls = 1, no entries",
                    1,
                    emptyList(),
                    Severity.OK,
                    0,
                    0,
                ),
            ).forEach { (description, numberOfControls, reportEntries, expectedSeverity, expectedCount, expectedUnique) ->
                When(description) {
                    val sut =
                        ValidationResult(
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
