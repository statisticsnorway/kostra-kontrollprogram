package no.ssb.kostra.program.extension

fun Int.yearWithCentury() = when (this) {
    in 1..99 -> 2_000 + this
    else -> this
}

