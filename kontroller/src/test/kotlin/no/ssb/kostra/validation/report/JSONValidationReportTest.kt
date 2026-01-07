package no.ssb.kostra.validation.report

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.program.KotlinArguments
import java.time.LocalDateTime

class JSONValidationReportTest : BehaviorSpec({
    val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    Given("StructuredValidationReport with no report entries") {
        When("generating JSON report") {
            val startTime = LocalDateTime.of(2023, 1, 1, 10, 0, 0)
            val validationReportArguments = ValidationReportArguments(
                kotlinArguments = KotlinArguments(
                    skjema = "0A",
                    aargang = "2023",
                    region = "123456",
                    navn = "Test Municipality",
                    startTime = startTime,
                ),
                validationResult = ValidationResult(
                    reportEntries = emptyList(),
                    numberOfControls = 5
                )
            )

            val sut = StructuredValidationReport(
                validationReportArguments = validationReportArguments,
                gitProperties = GitProperties("v1.0.0")
            )

            val result = sut.toString()

            Then("JSON should contain all required fields") {
                result shouldContain "validationSummary"
                result shouldContain "reportEntries"
                result shouldContain "region"
                result shouldContain "name"
                result shouldContain "scheme"
                result shouldContain "year"
                result shouldContain "numberOfControls"
                result shouldContain "programVersion"
                result shouldContain "reportStartedAt"
                result shouldContain "reportCompletedAt"
                result shouldContain "reportGenerationTime"
                result shouldContain "severity"
            }

            Then("JSON should have correct values") {
                result shouldContain "\"region\" : \"123456\""
                result shouldContain "\"name\" : \"Test Municipality\""
                result shouldContain "\"scheme\" : \"0A\""
                result shouldContain "\"year\" : \"2023\""
                result shouldContain "\"numberOfControls\" : 5"
                result shouldContain "\"programVersion\" : \"v1.0.0\""
                result shouldContain "\"numFatal\" : 0"
                result shouldContain "\"numErrors\" : 0"
                result shouldContain "\"numWarnings\" : 0"
                result shouldContain "\"numInfo\" : 0"
                result shouldContain "\"numOk\" : 0"
                result shouldContain "\"severity\" : 0"
            }

            Then("JSON should be parseable") {
                val parsed = mapper.readValue<StructuredValidationContainer>(result)
                parsed.validationSummary.region shouldBe "123456"
                parsed.validationSummary.name shouldBe "Test Municipality"
                parsed.validationSummary.scheme shouldBe "0A"
                parsed.validationSummary.year shouldBe "2023"
                parsed.validationSummary.numberOfControls shouldBe 5
                parsed.validationSummary.programVersion shouldBe "v1.0.0"
                parsed.reportEntries shouldBe emptyList()
            }
        }
    }

    Given("StructuredValidationReport with entries of different severities") {
        forAll(
            row(
                "FATAL entries",
                listOf(
                    ValidationReportEntry(severity = Severity.FATAL, ruleName = "Rule1", messageText = "Fatal error"),
                    ValidationReportEntry(severity = Severity.FATAL, ruleName = "Rule2", messageText = "Another fatal error")
                ),
                2, 0, 0, 0, 0, 2
            ),
            row(
                "ERROR entries",
                listOf(
                    ValidationReportEntry(severity = Severity.ERROR, ruleName = "Rule1", messageText = "Error"),
                    ValidationReportEntry(severity = Severity.ERROR, ruleName = "Rule2", messageText = "Another error"),
                    ValidationReportEntry(severity = Severity.ERROR, ruleName = "Rule3", messageText = "Yet another error")
                ),
                0, 3, 0, 0, 0, 2
            ),
            row(
                "WARNING entries",
                listOf(
                    ValidationReportEntry(severity = Severity.WARNING, ruleName = "Rule1", messageText = "Warning")
                ),
                0, 0, 1, 0, 0, 1
            ),
            row(
                "INFO entries",
                listOf(
                    ValidationReportEntry(severity = Severity.INFO, ruleName = "Rule1", messageText = "Info"),
                    ValidationReportEntry(severity = Severity.INFO, ruleName = "Rule2", messageText = "Another info")
                ),
                0, 0, 0, 2, 0, 0
            ),
            row(
                "OK entries",
                listOf(
                    ValidationReportEntry(severity = Severity.OK, ruleName = "Rule1", messageText = "OK")
                ),
                0, 0, 0, 0, 1, 0
            ),
            row(
                "Mixed severity entries",
                listOf(
                    ValidationReportEntry(severity = Severity.FATAL, ruleName = "Rule1", messageText = "Fatal"),
                    ValidationReportEntry(severity = Severity.ERROR, ruleName = "Rule2", messageText = "Error"),
                    ValidationReportEntry(severity = Severity.WARNING, ruleName = "Rule3", messageText = "Warning"),
                    ValidationReportEntry(severity = Severity.INFO, ruleName = "Rule4", messageText = "Info"),
                    ValidationReportEntry(severity = Severity.OK, ruleName = "Rule5", messageText = "OK")
                ),
                1, 1, 1, 1, 1, 2
            )
        ) { description, reportEntries, expectedFatal, expectedError, expectedWarning, expectedInfo, expectedOk, expectedSeverity ->
            When(description) {
                val validationReportArguments = ValidationReportArguments(
                    kotlinArguments = KotlinArguments(
                        skjema = "0A",
                        aargang = "2023",
                        region = "123456",
                        startTime = LocalDateTime.of(2023, 1, 1, 10, 0, 0)
                    ),
                    validationResult = ValidationResult(
                        reportEntries = reportEntries,
                        numberOfControls = 10
                    )
                )

                val sut = StructuredValidationReport(
                    validationReportArguments = validationReportArguments,
                    gitProperties = GitProperties("v1.0.0")
                )

                val result = sut.toString()

                Then("severity counts should be correct") {
                    val parsed = mapper.readValue<StructuredValidationContainer>(result)
                    parsed.validationSummary.numFatal shouldBe expectedFatal
                    parsed.validationSummary.numErrors shouldBe expectedError
                    parsed.validationSummary.numWarnings shouldBe expectedWarning
                    parsed.validationSummary.numInfo shouldBe expectedInfo
                    parsed.validationSummary.numOk shouldBe expectedOk
                    parsed.validationSummary.severity shouldBe expectedSeverity
                    parsed.reportEntries shouldBe reportEntries
                }
            }
        }
    }

    Given("StructuredValidationReport with complex report entries") {
        When("entries have all fields populated") {
            val reportEntries = listOf(
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    caseworker = "worker1",
                    journalId = "journal123",
                    individId = "indiv456",
                    contextId = "context789",
                    ruleName = "Rule ABC",
                    messageText = "Detailed error message",
                    lineNumbers = listOf(1, 2, 3)
                ),
                ValidationReportEntry(
                    severity = Severity.WARNING,
                    caseworker = "worker2",
                    journalId = "journal456",
                    individId = "indiv789",
                    contextId = "context012",
                    ruleName = "Rule XYZ",
                    messageText = "Warning message",
                    lineNumbers = listOf(5, 10, 15)
                )
            )

            val validationReportArguments = ValidationReportArguments(
                kotlinArguments = KotlinArguments(
                    skjema = "0B",
                    aargang = "2024",
                    region = "654321",
                    navn = "Another Municipality",
                    startTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
                ),
                validationResult = ValidationResult(
                    reportEntries = reportEntries,
                    numberOfControls = 20
                )
            )

            val sut = StructuredValidationReport(
                validationReportArguments = validationReportArguments,
                gitProperties = GitProperties("v2.0.0")
            )

            val result = sut.toString()

            Then("all report entry fields should be preserved in JSON") {
                val parsed = mapper.readValue<StructuredValidationContainer>(result)
                parsed.reportEntries.size shouldBe 2

                val firstEntry = parsed.reportEntries[0]
                firstEntry.severity shouldBe Severity.ERROR
                firstEntry.caseworker shouldBe "worker1"
                firstEntry.journalId shouldBe "journal123"
                firstEntry.individId shouldBe "indiv456"
                firstEntry.contextId shouldBe "context789"
                firstEntry.ruleName shouldBe "Rule ABC"
                firstEntry.messageText shouldBe "Detailed error message"
                firstEntry.lineNumbers shouldBe listOf(1, 2, 3)

                val secondEntry = parsed.reportEntries[1]
                secondEntry.severity shouldBe Severity.WARNING
                secondEntry.caseworker shouldBe "worker2"
                secondEntry.journalId shouldBe "journal456"
            }
        }
    }

    Given("StructuredValidationReport timestamp and duration handling") {
        When("generating report with specific timestamps") {
            val startTime = LocalDateTime.of(2023, 3, 15, 9, 30, 0)

            val validationReportArguments = ValidationReportArguments(
                kotlinArguments = KotlinArguments(
                    skjema = "0A",
                    aargang = "2023",
                    region = "123456",
                    startTime = startTime
                ),
                validationResult = ValidationResult(
                    reportEntries = emptyList(),
                    numberOfControls = 1
                )
            )

            val sut = StructuredValidationReport(
                validationReportArguments = validationReportArguments,
                gitProperties = GitProperties("v1.0.0")
            )

            val result = sut.toString()

            Then("timestamps should be formatted correctly") {
                result shouldContain "reportStartedAt"
                result shouldContain "reportCompletedAt"
                result shouldContain "reportGenerationTime"

                val parsed = mapper.readValue<StructuredValidationContainer>(result)
                parsed.validationSummary.reportStartedAt shouldBe startTime
            }
        }
    }

    Given("StructuredValidationReport with default git properties") {
        When("no custom git properties provided") {
            val validationReportArguments = ValidationReportArguments(
                kotlinArguments = KotlinArguments(
                    skjema = "0A",
                    aargang = "2023",
                    region = "123456",
                    startTime = LocalDateTime.of(2023, 1, 1, 10, 0, 0)
                ),
                validationResult = ValidationResult(
                    reportEntries = emptyList(),
                    numberOfControls = 1
                )
            )

            val sut = StructuredValidationReport(
                validationReportArguments = validationReportArguments
            )

            val result = sut.toString()

            Then("report should be generated successfully") {
                result shouldContain "validationSummary"
                result shouldContain "programVersion"
            }
        }
    }
})