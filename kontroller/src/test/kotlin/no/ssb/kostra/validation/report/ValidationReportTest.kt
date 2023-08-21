package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.program.KotlinArguments
import java.time.LocalDateTime

class ValidationReportTest : BehaviorSpec({
    val kotlinArguments = KotlinArguments(
        skjema = "0A",
        aargang = "2023",
        region = "1234",
        startTime = LocalDateTime.of(2022, 1, 1, 1, 1, 0),
    )

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
                        kotlinArguments = kotlinArguments,
                        validationResult = ValidationResult(
                            reportEntries = reportEntries,
                            numberOfControls = numberOfControls,
                        )
                    )
                )

                println(sut.toString())
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
                    kotlinArguments = kotlinArguments,
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

})