package no.ssb.kostra.area.sosial.extension

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

fun MutableMap<String, MutableList<String>>.addKeyOrAddValueIfKeyIsPresent(key: String, value: String) {
    if (this.containsKey(key)) this[key]!!.add(value)
    else this[key] = mutableListOf()
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
