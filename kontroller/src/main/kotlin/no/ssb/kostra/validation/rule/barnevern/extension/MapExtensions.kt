package no.ssb.kostra.validation.rule.barnevern.extension

fun MutableMap<String, MutableList<String>>.addKeyOrAddValueIfKeyIsPresent(key: String, value: String) {
    when (val entry = this[key]) {
        null -> this[key] = mutableListOf(value)
        else -> entry.add(value)
    }
}