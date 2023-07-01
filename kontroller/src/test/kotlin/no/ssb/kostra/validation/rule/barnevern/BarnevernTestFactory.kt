package no.ssb.kostra.validation.rule.barnevern

import io.kotest.core.spec.style.behaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

data class ForAllRowItem<T : Any>(
    val description: String,
    val context: T,
    val expectError: Boolean,
    val expectedErrorMessage: String? = null,
    val arguments: KotlinArguments = argumentsInTest
)

object BarnevernTestFactory {

    fun <T : Any> barnevernValidationRuleTest(
        sut: AbstractRule<T>,
        forAllRows: Collection<ForAllRowItem<T>>,
        expectedSeverity: Severity,
        expectedErrorMessage: String,
        expectedContextId: String? = null
    ) = behaviorSpec {
        Given("context") {
            forAll(
                *(forAllRows.map { (description, avgiver, expectError, expectedErrorMessage, arguments) ->
                    row(description, avgiver, expectError, expectedErrorMessage, arguments)
                }).toTypedArray()
            ) { description, context, expectError,innerExpectedErrorMessage, arguments ->
                When(description) {
                    val validationReportEntries = sut.validate(context, arguments)

                    Then("result should be as expected") {
                        verifyValidationResult(
                            validationReportEntries = validationReportEntries,
                            expectError = expectError,
                            expectedSeverity = expectedSeverity,
                            expectedErrorText = innerExpectedErrorMessage ?: expectedErrorMessage
                        )
                    }

                    And("if expectedContextId is present") {
                        if (expectError
                            && expectedContextId != null
                            && context is KostraIndividType
                        ) {
                            validationReportEntries.shouldNotBeNull()
                            validationReportEntries.first().contextId.shouldBe(expectedContextId)
                        }
                    }
                }
            }
        }
    }
}