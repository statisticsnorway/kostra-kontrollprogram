package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

open class AbstractRule<in T : Any>(
    protected val ruleName: String,
    private val severity: Severity
) {
    open fun validate(context: T): List<ValidationReportEntry>? = null

    open fun validate(context: T, arguments: KotlinArguments): List<ValidationReportEntry>? = null

    /** used by record-based rules */
    protected fun createValidationReportEntry(
        messageText: String,
        lineNumbers: List<Int> = emptyList(),
        contextId: String = "",
    ) = ValidationReportEntry(
        severity = severity,
        ruleName = ruleName,
        messageText = messageText,
        lineNumbers = lineNumbers,
        contextId = contextId,
    )

    protected fun createValidationReportEntry(
        ruleName: String,
        messageText: String,
        lineNumbers: List<Int> = emptyList(),
        contextId: String = "",
    ) = ValidationReportEntry(
        severity = severity,
        ruleName = ruleName,
        messageText = messageText,
        lineNumbers = lineNumbers,
        contextId = contextId,
    )

    protected fun createSingleReportEntryList(
        messageText: String,
        contextId: String = ""
    ) = listOf(
        ValidationReportEntry(
            ruleName = ruleName,
            severity = severity,
            contextId = contextId,
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


    protected fun createSingleReportEntryList(
        messageText: String,
        lineNumbers: List<Int>
    ) = listOf(
        ValidationReportEntry(
            ruleName = ruleName,
            severity = severity,
            messageText = messageText,
            lineNumbers = lineNumbers
        )
    )
}