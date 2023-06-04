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
        lineNumbers: List<Int> = emptyList()
    ) = ValidationReportEntry(
        severity = severity,
        ruleName = ruleName,
        messageText = messageText,
        lineNumbers = lineNumbers
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

    protected fun createValidationReportEntry(
        journalId: String,
        contextId: String,
        messageText: String,
    ) = ValidationReportEntry(
        ruleName = ruleName,
        severity = severity,
        journalId = journalId,
        contextId = contextId,
        messageText = messageText
    )

    protected fun createSingleReportEntryList(
        journalId: String,
        contextId: String,
        messageText: String,
    ) = listOf(
        ValidationReportEntry(
            ruleName = ruleName,
            severity = severity,
            journalId = journalId,
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
        lineNumbers: List<Int> = emptyList()
    ) = listOf(createValidationReportEntry(messageText, lineNumbers))
}