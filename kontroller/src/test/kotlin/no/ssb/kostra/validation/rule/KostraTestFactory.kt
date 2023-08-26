package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.behaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

data class ForAllRowItem<out T : Any>(
    val description: String,
    val context: T,
    val expectedErrorMessage: String? = null,
    val arguments: KotlinArguments = argumentsInTest,
    val expectedSize: Int = 1
)

object KostraTestFactory {
    fun <T : Any> validationRuleNoContextTest(
        sut: AbstractRule<T>,
        expectedSeverity: Severity,
        vararg forAllRows: ForAllRowItem<T>
    ) = validationRuleTest(
        sut = sut,
        expectedSeverity = expectedSeverity,
        expectedContextId = null,
        forAllRows = forAllRows
    )

    fun <T : Any> validationRuleWithArgsTest(
        sut: AbstractRule<T>,
        expectedSeverity: Severity,
        expectedContextId: String?,
        vararg forAllRows: ForAllRowItem<T>
    ) = validationRuleTest(
        sut = sut,
        expectedSeverity = expectedSeverity,
        expectedContextId = expectedContextId,
        forAllRows = forAllRows
    )

    fun <T : Any> validationRuleNoArgsTest(
        sut: AbstractRule<T>,
        expectedSeverity: Severity,
        vararg forAllRows: ForAllRowItem<T>
    ) = validationRuleTest(
        sut = sut,
        expectedSeverity = expectedSeverity,
        expectedContextId = null,
        forAllRows = forAllRows
    )

    private fun <T : Any> validationRuleTest(
        sut: AbstractRule<T>,
        expectedSeverity: Severity,
        expectedContextId: String?,
        vararg forAllRows: ForAllRowItem<T>
    ) = behaviorSpec {
        Given("context") {
            forAll(
                *forAllRows.map { (description, context, expectedErrorMessage, arguments, expectedSize) ->
                    row(description, context, expectedErrorMessage, arguments, expectedSize)
                }.toTypedArray()
            ) { description, context, expectedErrorMessage, arguments, expectedSize ->
                When(description) {
                    val validationReportEntries = sut.validate(context, arguments)

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