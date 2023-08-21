package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ValidationResultTest : BehaviorSpec({
    Given("ValidationResult instance with 2 reportEntries and  empty statsReportEntries") {
        val sut = ValidationResult(
            reportEntries = listOf(
                ValidationReportEntry().copy(lineNumbers = listOf(1, 2)),
                ValidationReportEntry().copy(lineNumbers = listOf(2, 3)),
            ),
            numberOfControls = 1,
            statsReportEntries = emptyList(),
            endTime = LocalDateTime.of(2022,1,1, 1,1,1),
        )

        When("count is called") {
            val result = sut.count

            Then("result should be as expected") {
                result shouldBe 2
            }
        }

        When("severity is called") {
            val result = sut.severity

            Then("result should be as expected") {
                result shouldBe Severity.OK
            }
        }

        When("uniqueReportEntries is called") {
            val result = sut.uniqueReportEntries

            Then("result should be as expected") {
                result.size shouldBe 1
                result[0].lineNumbers shouldBe listOf(1, 2, 3)
            }
        }
    }
})