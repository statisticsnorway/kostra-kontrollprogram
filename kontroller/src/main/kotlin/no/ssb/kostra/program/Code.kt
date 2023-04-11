package no.ssb.kostra.program

data class Code(
    val code: String,
    val value: String
) {
    override fun toString() = "$code=$value"
}