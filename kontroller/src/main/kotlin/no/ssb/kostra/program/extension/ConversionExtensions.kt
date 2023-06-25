package no.ssb.kostra.program.extension

import no.ssb.kostra.validation.report.Severity

fun Int.toYearWithCentury() = when (this) {
    in 1..99 -> 2_000 + this
    else -> this
}

internal fun Severity.toInt() = when (this) {
    Severity.WARNING -> 1
    Severity.ERROR -> 2
    else -> 0
}


