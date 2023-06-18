package no.ssb.kostra.area.sosial.extension

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

fun MutableMap<String, MutableList<String>>.addKeyOrAddValueIfKeyIsPresent(key: String, value: String) {
    when (val entry = this[key]) {
        null -> this[key] = mutableListOf()
        else -> entry.add(value)
    }
}

fun Map<String, Collection<String>>.mapToValidationReportEntries(
    ruleName: String,
    messageText: String,
) = this.filterValues { it.any() }.map { entry ->
    ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = ruleName,
        messageText = "$messageText (${entry.value.joinToString(", ")})"
    )
}
