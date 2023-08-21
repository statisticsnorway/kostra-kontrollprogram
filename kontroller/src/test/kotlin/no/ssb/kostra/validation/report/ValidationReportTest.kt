package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.program.KotlinArguments
import java.time.LocalDateTime

class ValidationReportTest : BehaviorSpec({
    Given("ValidationReport only a minimum of arguments to pass the test") {
        forAll(
            row("", Severity.FATAL, )
        ){ description ->


        }

        val validationReportArguments = ValidationReportArguments(
            kotlinArguments = KotlinArguments(
                skjema = "0A",
                aargang = "2023",
                region = "1234",
                startTime = LocalDateTime.of(2022, 1, 1, 1, 1, 0),
            ),
            validationResult = ValidationResult(
                reportEntries = listOf(
                    ValidationReportEntry(
                        severity = Severity.ERROR,
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

        When("count is called") {
            val result = sut.toString()

            Then("result should be as expected") {
                result shouldContain "0A"
                result shouldContain "2023"
                result shouldContain "1234"
                result shouldContain "Kontroll Test 1"
                result shouldContain "LOCAL-SNAPSHOT"

            }
        }
    }
})