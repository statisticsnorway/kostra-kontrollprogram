package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KotlinArguments
import java.time.LocalDateTime

class ValidationReportTest : BehaviorSpec({
    Given("ValidationReport, summary per validation, branch") {
        forAll(
            row("Finner ingen data", 0, emptyList()),
            row("Ingen feil funnet", 1, emptyList()),
            row(
                "Oppsummering pr. kontroll", 1, listOf(
                    ValidationReportEntry().copy(
                        ruleName = "Rule 1",
                        messageText = "Message text",
                        lineNumbers = listOf(1)
                    )
                )
            ),
        ) { description, numberOfControls, reportEntries ->
            When(description) {
                val sut = ValidationReport(
                    validationReportArguments = ValidationReportArguments(
                        kotlinArguments = kotlinArgumentsInTest,
                        validationResult = ValidationResult(
                            reportEntries = reportEntries,
                            numberOfControls = numberOfControls,
                        )
                    )
                )

                Then("result should be as expected") {
                    sut.toString() shouldContain description
                }
            }
        }
    }

    Given("ValidationReport, summary per validation, severity") {
        forAll(
            row(Severity.FATAL, "kontrollene i bli kjørt</div>"),
            row(Severity.ERROR, "feil som hindrer innsending</div>"),
            row(Severity.WARNING, "advarsler</div>"),
            row(Severity.INFO, "informasjonsmeldinger</div>"),
            row(Severity.OK, "meldinger</div>"),
        ) { severity, expectedMessaage ->
            When(severity.name) {
                val validationReportArguments = ValidationReportArguments(
                    kotlinArguments = kotlinArgumentsInTest.copy(
                        isRunAsExternalProcess = false
                    ),
                    validationResult = ValidationResult(
                        reportEntries = listOf(
                            ValidationReportEntry(
                                severity = severity,
                                ruleName = "Kontroll Test 1",
                                messageText = "Feilmelding",
                                lineNumbers = listOf(1)
                            )
                        ),
                        numberOfControls = 1,
                        endTime = LocalDateTime.of(2022, 1, 1, 1, 1, 1),
                    )
                )
                val sut = ValidationReport(validationReportArguments = validationReportArguments)

                val result = sut.toString()

                Then("result should be as expected") {
                    result shouldContain expectedMessaage
                    result shouldContain "color: ${severity.info.color}"
                }
            }
        }
    }

    Given("ValidationReport, show stats") {
        forAll(
            row(
                "has errors, no stats shown",
                Severity.ERROR,
                emptyList(),
                "Oppsummering pr. kontroll"
            ),
            row(
                "has no errors, no shown",
                Severity.INFO,
                listOf(
                    StatsReportEntry(
                        heading = StatsEntryHeading("&nbsp;","~content~"),
                        entries = listOf(StatsEntry("first", "123"))
                    )
                ),
                "Oppsummering pr. kontroll"
            ),
        ) { description, severity, statsReportEntries, expectedMessage ->
            When(description) {
                val sut = ValidationReport(
                    validationReportArguments = ValidationReportArguments(
                        kotlinArguments = kotlinArgumentsInTest,
                        validationResult = ValidationResult(
                            reportEntries = listOf(ValidationReportEntry(severity = severity)),
                            numberOfControls = 1,
                            statsReportEntries = statsReportEntries
                        )
                    )
                )

                Then("result should be as expected") {
                    sut.toString() shouldContain expectedMessage
                }
            }
        }
    }
}) {
    companion object {
        private val kotlinArgumentsInTest = KotlinArguments(
            skjema = "0A",
            aargang = "2023",
            region = "1234",
            startTime = LocalDateTime.of(2022, 1, 1, 1, 1, 0),
            isRunAsExternalProcess = true
        )
    }
}