package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity

class ControlDispatcherTest : BehaviorSpec({
    Given("ValidationResult instance with 2 reportEntries and  empty statsReportEntries") {
        forAll(
            row("0AK1"),
            row("0A"),
            row("0F"),
            row("0X"),
            row("11F"),
            row("11CF"),

            row("52AF"),
            row("52BF"),
            row("53F"),
            row("55F"),
            row("FAIL"),

            ) { schema ->
            When(schema) {
                val kotlinArguments = KotlinArguments(
                    skjema = schema,
                    aargang = "2023",
                    region = "1234"
                )

                val validationReportArguments = ControlDispatcher.validate(kotlinArguments = kotlinArguments)

                Then("result should be as expected") {
                    validationReportArguments.validationResult.severity shouldBe Severity.FATAL
                }
            }
        }

        forAll(
            row("15F"),

            ) { schema ->
            When(schema) {
                val kotlinArguments = KotlinArguments(
                    skjema = schema,
                    aargang = "2023",
                    region = "1234"
                )

                val validationReportArguments = ControlDispatcher.validate(kotlinArguments = kotlinArguments)

                Then("result should be as expected") {
                    validationReportArguments.validationResult.severity shouldBe Severity.ERROR
                }
            }
        }
    }
})