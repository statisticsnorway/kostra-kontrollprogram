package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity

class ControlDispatcherTest :
    BehaviorSpec({
        Given("ValidationResult instance with 2 reportEntries and  empty statsReportEntries") {
            listOf(
                "0AK1",
                "0A",
                "0B",
                "0I",
                "0J",
                "0N",
                "0Q",
                "0F",
                "0X",
                "11F",
                "11CF",
                "52AF",
                "52BF",
                "53F",
                "55F",
                "FAIL",
            ).forEach { schema ->
                When(schema) {
                    val kotlinArguments =
                        KotlinArguments(
                            skjema = schema,
                            aargang = "2023",
                            region = "1234",
                        )

                    val validationReportArguments = ControlDispatcher.validate(kotlinArguments = kotlinArguments)

                    Then("result should be as expected") {
                        validationReportArguments.validationResult.severity shouldBe Severity.FATAL
                    }
                }
            }
        }
    })
