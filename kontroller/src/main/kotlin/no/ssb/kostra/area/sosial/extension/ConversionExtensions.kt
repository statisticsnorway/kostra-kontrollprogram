package no.ssb.kostra.area.sosial.extension

fun Int.toFourDigitYear() = when (this) {
    in 1..99 -> 2_000 + this
    else -> this
}
