package no.ssb.kostra.validation.rule.barnevern

import io.kotest.core.spec.style.behaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

object BarnevernTestFactory {

    fun <T : Any> barnevernValidationRuleTest(
        sut: AbstractRule<T>,
        forAllRows: Collection<Triple<String, T, Boolean>>,
        expectedSeverity: Severity,
        expectedErrorMessage: String
    ) = behaviorSpec {
        Given("context") {
            forAll(
                *(forAllRows.map { (description, avgiver, expectError) ->
                    row(description, avgiver, expectError)
                }).toTypedArray()
            ) { description, avgiver, expectError ->
                When(description) {
                    verifyValidationResult(
                        validationReportEntries = sut.validate(avgiver, argumentsInTest),
                        expectError = expectError,
                        expectedSeverity = expectedSeverity,
                        expectedErrorText = expectedErrorMessage
                    )
                }
            }
        }
    }
}