package no.ssb.kostra.validation.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

open class AbstractRule<T>(
    private val ruleName: String,
    private val severity: Severity
) {
    open fun validate(context: List<T>): List<ValidationReportEntry>? = null
    protected fun createValidationReportEntry(
        messageText: String,
        lineNumbers: List<Int> = emptyList()
    ) = ValidationReportEntry(
        severity = severity,
        ruleName = ruleName,
        messageText = messageText,
        lineNumbers = lineNumbers
    )

    protected fun createValidationReportEntry(
        messageText: String
    ) = ValidationReportEntry(
        severity = severity,
        ruleName = ruleName,
        messageText = messageText
    )

    protected fun createValidationReportEntry(
        caseworker: String,
        journalId: String,
        messageText: String,
        lineNumbers: List<Int> = emptyList()
    ) = ValidationReportEntry(
        severity = severity,
        caseworker = caseworker,
        journalId = journalId,
        ruleName = ruleName,
        messageText = messageText,
        lineNumbers = lineNumbers
    )

    protected fun createSingleReportEntryList(
        messageText: String
    ) = listOf(createValidationReportEntry(messageText))

    protected fun createSingleReportEntryList(
        messageText: String,
        lineNumbers: List<Int> = emptyList()
    ) = listOf(createValidationReportEntry(messageText, lineNumbers))

}