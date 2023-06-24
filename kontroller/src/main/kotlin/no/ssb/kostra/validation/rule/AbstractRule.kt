package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

open class AbstractRule<in T : Any>(
    private val ruleName: String,
    private val severity: Severity
) {
    open fun validate(context: List<T>): List<ValidationReportEntry>? = null

    open fun validate(context: T, arguments: KotlinArguments): List<ValidationReportEntry>? = null

    protected fun createValidationReportEntry(
        messageText: String,
        lineNumbers: List<Int>
    ) = ValidationReportEntry(
        severity = severity,
        ruleName = ruleName,
        messageText = messageText,
        lineNumbers = lineNumbers
    )

    protected fun createValidationReportEntry(
        contextId: String,
        messageText: String,
    ) = ValidationReportEntry(
        ruleName = ruleName,
        severity = severity,
        contextId = contextId,
        messageText = messageText
    )

    protected fun createSingleReportEntryList(
        contextId: String,
        messageText: String,
    ) = listOf(
        ValidationReportEntry(
            ruleName = ruleName,
            severity = severity,
            contextId = contextId,
            messageText = messageText
        )
    )

    protected fun createSingleReportEntryList(
        messageText: String
    ) = listOf(
        ValidationReportEntry(
            ruleName = ruleName,
            severity = severity,
            messageText = messageText
        )
    )

    protected fun createSingleReportEntryList(
        messageText: String,
        severity: Severity
    ) = listOf(
        ValidationReportEntry(
            ruleName = ruleName,
            severity = severity,
            messageText = messageText
        )
    )
}