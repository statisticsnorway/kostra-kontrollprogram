package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.behaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import kotlin.math.exp

data class ForAllRowItem<out T : Any>(
    val description: String,
    val context: T,
    val expectedErrorMessage: String? = null,
    val arguments: KotlinArguments = argumentsInTest,
    val expectedSize: Int = 1
)

object KostraTestFactory {

    fun <T : Any> validationRuleTest(
        sut: AbstractRule<T>,
        forAllRows: Collection<ForAllRowItem<T>>,
        expectedSeverity: Severity,
        expectedContextId: String? = null,
        useArguments: Boolean = true
    ) = behaviorSpec {
        Given("context") {
            forAll(
                *forAllRows.map { (description, context, expectedErrorMessage, arguments, expectedSize) ->
                    row(description, context, expectedErrorMessage, arguments, expectedSize)
                }.toTypedArray()
            ) { description, context, expectedErrorMessage, arguments, expectedSize ->
                When(description) {
                    val validationReportEntries =
                        if (useArguments) sut.validate(context, arguments)
                        else sut.validate(context)

                    Then("result should be as expected") {
                        verifyValidationResult(
                            validationReportEntries = validationReportEntries,
                            expectError = !expectedErrorMessage.isNullOrEmpty(),
                            expectedSeverity = expectedSeverity,
                            expectedErrorText = expectedErrorMessage ?: "N/A",
                            expectedSize = expectedSize
                        )

                        /** if expectedContextId is present */
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