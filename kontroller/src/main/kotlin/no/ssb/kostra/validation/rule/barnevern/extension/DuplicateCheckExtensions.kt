package no.ssb.kostra.validation.rule.barnevern.extension

import no.ssb.kostra.validation.report.ValidationReportEntry

fun <T : Any> Collection<T>.runDuplicateCheck(
    areEqualFunc: (T, T) -> Boolean,
    reportEntryProducerFunc: (T, T) -> ValidationReportEntry
): List<ValidationReportEntry>? = this
    .takeIf { it.size > 1 }?.let {
        ArrayDeque(this).let { unprocessedItems ->
            mutableListOf<ValidationReportEntry>().apply {
                do {
                    addAll(
                        unprocessedItems.removeFirst().let { outerElement ->
                            unprocessedItems
                                .filter { innerElement -> areEqualFunc(outerElement, innerElement) }
                                .map { reportEntryProducerFunc(outerElement, it) }.distinct()
                        })
                } while (unprocessedItems.size > 1)
            }.ifEmpty { null }
        }
    }
