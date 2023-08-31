package no.ssb.kostra.validation.rule

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.behaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

private const val NO_ERROR = "N/A"

data class ForAllRowItem<out T : Any>(
    val description: String,
    val context: T,
    val expectedErrors: List<Pair<String, String>>,
    val arguments: KotlinArguments = argumentsInTest,
    val expectedSeverity: Severity? = null,
    val expectedSize: Int? = null
) {

    constructor(
        description: String,
        context: T,
        expectedErrorMessage: String = NO_ERROR,
        arguments: KotlinArguments = argumentsInTest,
        expectedSeverity: Severity? = null,
        expectedSize: Int? = null
    ) : this(
        description = description,
        context = context,
        expectedErrors = listOf("" to expectedErrorMessage),
        arguments = arguments,
        expectedSeverity = expectedSeverity,
        expectedSize = expectedSize
    )

    constructor(
        description: String,
        context: T,
        vararg expectedErrors: Pair<String, String>
    ) : this(
        description = description,
        context = context,
        expectedErrors = expectedErrors.toList()
    )
}

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
        expectedContextId: String? = null,
        vararg forAllRows: ForAllRowItem<T>
    ) = behaviorSpec {
        Given("context") {
            forAll(
                *forAllRows.map { (description, context, expectedErrors, arguments, expectedSeverity, expectedSize) ->
                    row(description, context, expectedErrors, arguments, expectedSeverity, expectedSize)
                }.toTypedArray()
            ) { description, context, expectedErrors, arguments, innerExpectedSeverity, expectedSize ->
                When(description) {
                    val validationReportEntries = sut.validate(context, arguments)

                    withClue({ "Tests need to have one or more test runs" }) {
                        assertSoftly(expectedErrors) {
                            shouldNotBeNull()
                            shouldNotBeEmpty()
                        }
                    }

                    val numberOfExpectedErrors = when (expectedSize) {
                        null -> expectedErrors.count { (_, errorMessage) -> errorMessage != NO_ERROR }
                        else -> expectedSize
                    }

                    val thenDescription =
                        if (numberOfExpectedErrors > 0) "reportEntries should contain $numberOfExpectedErrors error(s)"
                        else "reportEntries should be null"

                    /** if contextId is not provided in [expectedErrors], use default */
                    val testRuns = expectedErrors.map { (innerExpectedContextId, expectedErrorMessage) ->
                        (when (innerExpectedContextId) {
                            "" -> expectedContextId ?: innerExpectedContextId
                            else -> innerExpectedContextId
                        }) to expectedErrorMessage
                    }

                    Then(thenDescription) {
                        testRuns.forAll { (expectedContextId, expectedErrorMessage) ->
                            verifyValidationResult(
                                validationReportEntries = validationReportEntries,
                                numberOfExpectedErrors = numberOfExpectedErrors,
                                expectedSeverity = innerExpectedSeverity ?: expectedSeverity,
                                expectedErrorText = expectedErrorMessage,
                                expectedContextId = expectedContextId
                            )
                        }
                    }
                }
            }
        }
    }
}