package gradletask.extensions

import gradletask.FileDescriptionTemplate
import gradletask.createYAMLMapper
import gradletask.toFileDescription

fun String.toFileDescriptionTemplate(): FileDescriptionTemplate =
    createYAMLMapper().readValue(this, FileDescriptionTemplate::class.java)
        ?: throw ClassCastException()


fun Pair<FileDescriptionTemplate, FileDescriptionTemplate>.toChangeLogMarkdown() =
    buildString {
        val fieldsA = first.toFileDescription().fields
        val fieldsB = second.toFileDescription().fields
        val namesA = fieldsA.map { it.name }.toSet()
        val namesB = fieldsB.map { it.name }.toSet()
        val checkmark = "\u2705"
        val crossmark = "\u274C"
        appendLine("# Endringslogg: ${first.title}, fra  ${first.reportingYear} til ${second.reportingYear}")
        appendLine()

        val addedFields = namesB - namesA

        if (addedFields.isNotEmpty()) {
            appendLine()
            appendLine("## Lagt til")
            appendLine("| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |")
            appendLine("|------|-------------|--------|---------|----------|--------------|------------|-----------|")

            addedFields
                .map { fieldsB.first { field -> field.name == it } }
                .forEach { field ->
                    val desc =
                        field.description
                            ?.take(300)
                            ?.replace("\n", "<br/>")
                            ?.let { if (it.length == 300) "$it..." else it }
                            ?: ""
                    val codes =
                        field.codeList
                            .joinToString("<br/>") { "`" + it.code + "`: " + it.value }
                    appendLine("| `${field.name}` | $desc | ${field.size} | ${field.from}‑${field.to} | ${field.dataType} | ${if (field.mandatory) checkmark else ""} | ${field.datePattern} | $codes |")
                }
        }

        val removedFields = namesA - namesB

        if (removedFields.isNotEmpty()) {
            appendLine()
            appendLine("## Fjernet")
            appendLine("| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |")
            appendLine("|------|-------------|--------|---------|----------|--------------|------------|-----------|")

            removedFields
                .map { fieldsA.first { field -> field.name == it } }
                .forEach { field ->
                    val desc =
                        field.description
                            ?.take(300)
                            ?.replace("\n", "<br/>")
                            ?.let { if (it.length == 300) "$it..." else it }
                            ?: ""
                    val codes =
                        field.codeList
                            .joinToString("<br/>") { "`" + it.code + "`: " + it.value }
                    appendLine("| `${field.name}` | $desc | ${field.size} | ${field.from}‑${field.to} | ${field.dataType} | ${if (field.mandatory) checkmark else ""} | ${field.datePattern} | $codes |")
                }

            appendLine("## Fjernede felt")
            removedFields.forEach { appendLine("- $it") }
        }

        val changedFields = namesA.intersect(namesB)
            .map {
                (fieldsA.first { field -> field.name == it }
                        to fieldsB.first { field -> field.name == it })
            }
            .filter { (a, b) -> a != b }
        if (changedFields.isNotEmpty()) {
            val changedTo = "<br/> endret til <br/>"
            appendLine()
            appendLine("## Endret")
            appendLine("| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |")
            appendLine("|------|-------------|--------|---------|----------|--------------|------------|-----------|")

            changedFields.forEach { (a, b) ->
                val name = a.name
                val description =
                    if (a.description == b.description) a.description ?: "" else "${a.description} $changedTo ${b.description}"
                val size =
                    if (a.size == b.size) a.size else "${a.size} $changedTo ${b.size}"
                val fromTo =
                    if (a.from == b.from && a.to == b.to) a.from.toString() else "${a.from}-${a.to} $changedTo ${b.from}-${b.to}"
                val dataType =
                    if (a.dataType == b.dataType) a.dataType else "${a.dataType} $changedTo ${b.dataType}"
                val mandatory =
                    if (a.mandatory) {
                        if (b.mandatory) { checkmark } else { "$checkmark $changedTo $crossmark" }
                    } else {
                        if (b.mandatory) { "$crossmark $changedTo $checkmark" } else { "" }
                    }
                val datePattern =
                    if (a.datePattern == b.datePattern) a.datePattern else "${a.datePattern} $changedTo ${b.datePattern}"
                val codes =
                    if (a.codeList == b.codeList)
                        "Ingen endringer"
                    else
                        first
                            .fields
                            .find { it.name == a.name }
                            ?.codeListSource
                            ?.let { klassId ->
                                "[Vis endringer](https://www.ssb.no/klass/klassifikasjoner/${klassId}/endringer)"
                            }

                appendLine("| `${name}` | $description | $size | $fromTo | $dataType | $mandatory | $datePattern | $codes |")
            }
        }

        if (addedFields.isEmpty() && removedFields.isEmpty() && changedFields.isEmpty()) {
            appendLine("## Ingen endringer")
        }
    }
