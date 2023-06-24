package no.ssb.kostra.program.extension

fun Int.toYearWithCentury() = when (this) {
    in 1..99 -> 2_000 + this
    else -> this
}
