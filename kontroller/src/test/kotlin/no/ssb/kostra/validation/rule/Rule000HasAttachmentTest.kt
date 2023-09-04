package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity

class Rule000HasAttachmentTest : BehaviorSpec({
    val arguments = KotlinArguments(
        skjema = "SS",
        aargang = "YYYY",
        region = "RRRR"
    )

    Given("Has attachment, has data and shall report") {
        val sut = Rule000HasAttachment()

        When("Has attachment is true, inputFileContent is present (other than a space)") {
            val result = sut.validate(
                context = emptyList(),
                arguments = arguments.copy(
                    harVedlegg = true,
                    inputFileContent = "OK"
                )
            )

            Then("result should be null") {
                result.shouldBeNull()
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "Has attachment is true, inputFileContent is missing (is only a space)",
                true,
                " ",
                Severity.FATAL,
            ),
            row(
                "Has attachment is false, inputFileContent is present (other than a space)",
                false,
                "FAIL",
                Severity.FATAL,
            ),
            row(
                "Has attachment is false, inputFileContent is missing (is only a space)",
                false,
                " ",
                Severity.OK,
            ),
        ) { description, hasAttachment, inputFileContent, expectedSeverity ->
            When(description) {
                val sut = Rule000HasAttachment()
                val result = sut.validate(
                    context = emptyList(),
                    arguments = arguments.copy(
                        harVedlegg = hasAttachment,
                        inputFileContent = inputFileContent
                    )
                )

                Then("result should be null") {
                    result.shouldNotBeNull()
                    result.size shouldBe 1
                    result.first().severity shouldBe expectedSeverity
                }
            }
        }
    }
})