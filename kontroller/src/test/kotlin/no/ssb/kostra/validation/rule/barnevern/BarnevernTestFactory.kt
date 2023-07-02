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
    val expectedErrorMessage: String? = null,
    val arguments: KotlinArguments = argumentsInTest
)

object BarnevernTestFactory {

    fun <T : Any> barnevernValidationRuleTest(
        sut: AbstractRule<T>,
        forAllRows: Collection<ForAllRowItem<T>>,
        expectedSeverity: Severity,
        expectedContextId: String? = null
    ) = behaviorSpec {
        Given("context") {
            forAll(
                *(forAllRows.map { (description, context, expectedErrorMessage, arguments) ->
                    row(description, context, expectedErrorMessage, arguments)
                }).toTypedArray()
            ) { description, context, expectedErrorMessage, arguments ->
                When(description) {
                    val validationReportEntries = sut.validate(context, arguments)

                    Then("result should be as expected") {
                        verifyValidationResult(
                            validationReportEntries = validationReportEntries,
                            expectError = !expectedErrorMessage.isNullOrEmpty(),
                            expectedSeverity = expectedSeverity,
                            expectedErrorText = expectedErrorMessage ?:"N/A"
                        )
                    }

                    And("if expectedContextId is present") {
                        if (context is KostraIndividType
                            && !expectedErrorMessage.isNullOrEmpty()
                            && expectedContextId != null
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