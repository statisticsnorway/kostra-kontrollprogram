package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FileDescription

fun FileDescription.toMarkdown(): String = buildString {
    appendLine("# Filbeskrivelse: $title (${reportingYear})")
    appendLine()
    appendLine(description)
    appendLine()
    appendLine("## Feltdefinisjoner")
    appendLine()
    appendLine("| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |")
    appendLine("|------|-------------|--------|---------|----------|--------------|------------|-----------|")
    for (field in fields) {
        val desc =
            field.description
                ?.take(300)
                ?.replace("\n", "<br/>")
                ?.let { if (it.length == 300) "$it..." else it } ?: ""
        val codes =
            field.codeList
                .joinToString("<br/>") { "`" + it.code + "`: " + it.value }
        appendLine("| `${field.name}` | $desc | ${field.size} | ${field.from}‑${field.to} | ${field.dataType} | ${if (field.mandatory) "☑\uFE0F" else ""} | ${field.datePattern} | $codes |")
    }
}
