package gradletask.extensions

import gradletask.FileDescriptionTemplate
import gradletask.createYAMLMapper
import no.ssb.kostra.SharedConstants.CHECKMARK
import no.ssb.kostra.SharedConstants.XMARK
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FileDescription

fun String.toFileDescriptionTemplate(): FileDescriptionTemplate =
    createYAMLMapper().readValue(this, FileDescriptionTemplate::class.java)
        ?: throw ClassCastException()

fun Pair<FileDescription, FileDescription>.toChangeLogMarkdown(): String {
    val that = this
    return buildString {
        val fieldsA = that.first.fields.associateBy { it.name }
        val fieldsB = that.second.fields.associateBy { it.name }
        appendLine("# Endringslogg: ${that.first.title}, fra  ${that.first.reportingYear} til ${that.second.reportingYear}")
        appendLine()

        val addedFields = fieldsB.keys - fieldsA.keys
        val removedFields = fieldsA.keys - fieldsB.keys
        val changedFields = fieldsA.keys.intersect(fieldsB.keys)
            .map { field -> (fieldsA[field] to fieldsB[field]) }
            .filter { (a, b) -> a != b }

        if (removedFields.isNotEmpty()) {
            append((fieldsA to fieldsB).toRemovedChangeLogMarkdown())
        }

        if (addedFields.isNotEmpty()) {
            append((fieldsA to fieldsB).toAddedChangeLogMarkdown())
        }

        if (changedFields.isNotEmpty()) {
            append((fieldsA to fieldsB).toChangedLogMarkdown())
        }

        if (addedFields.isEmpty() && removedFields.isEmpty() && changedFields.isEmpty()) {
            appendLine("## Ingen endringer")
        }
    }
}

fun Pair<Map<String, FieldDefinition>, Map<String, FieldDefinition>>.toAddedChangeLogMarkdown(): String {
    val that = this

    return buildString {
        val fieldsA = that.first
        val fieldsB = that.second
        val addedFields = fieldsB.keys - fieldsA.keys

        if (addedFields.isNotEmpty()) {
            appendLine()
            appendLine("## Lagt til")
            append(header())
            append(addedFields.mapNotNull { fieldsB[it] }.toMarkDownTable())
        }
    }
}

fun Pair<Map<String, FieldDefinition>, Map<String, FieldDefinition>>.toRemovedChangeLogMarkdown(): String {
    val that = this

    return buildString {
        val fieldsA = that.first
        val fieldsB = that.second
        val removedFields = fieldsA.keys - fieldsB.keys

        if (removedFields.isNotEmpty()) {
            appendLine()
            appendLine("## Fjernet")
            append(header())
            append(removedFields.mapNotNull { fieldsA[it] }.toMarkDownTable())
        }
    }
}

fun Pair<Map<String, FieldDefinition>, Map<String, FieldDefinition>>.toChangedLogMarkdown(): String {
    val that = this

    return buildString {
        val fieldsA = that.first
        val fieldsB = that.second
        val changedFields = fieldsA.keys.intersect(fieldsB.keys)
            .map { fieldsA[it]!! to fieldsB[it]!! }
            .filter { (a, b) -> a != b }

        if (changedFields.isNotEmpty()) {
            appendLine()
            appendLine("## Endret")
            append(header())

            changedFields.forEach { (a, b) ->
                val name = a.name
                val description =
                    "${a.description ?: ""}".changedTo("${b.description}")
                val size = a.size.toString().changedTo(b.size.toString())
                val fromTo = "${a.from}-${a.to}".changedTo("${b.from}-${b.to}")
                val dataType = "${a.dataType}".changedTo("${b.dataType}")
                val mandatory = a.mandatory.changedTo(b.mandatory)
                val datePattern = a.datePattern.changedTo(b.datePattern)
                val codes = a.codeList.changedTo(
                    b.codeList,
                    b.codelistSource
                )

                appendLine("| `$name` | $description | $size | $fromTo | $dataType | $mandatory | $datePattern | $codes |")
            }
        }
    }
}

fun header() = buildString {
    appendLine("| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |")
    appendLine("|------|-------------|--------|---------|----------|--------------|------------|-----------|")
}

fun List<FieldDefinition>.toMarkDownTable(): String {
    val that = this
    return buildString {
        that.forEach { field ->
            val desc =
                field.description
                    ?.take(300)
                    ?.replace("\n", "<br/>")
                    ?.let { if (it.length == 300) "$it..." else it }
                    ?: ""
            val codes =
                field.codeList
                    .joinToString("<br/>") { "`" + it.code + "`: " + it.value }
            appendLine("| `${field.name}` | $desc | ${field.size} | ${field.from}‑${field.to} | ${field.dataType} | ${if (field.mandatory) CHECKMARK else ""} | ${field.datePattern} | $codes |")
        }
    }
}

fun String.changedTo(other: String) =
    if (this == other) other else "$this<br/> endret til <br/>$other"

fun Boolean.changedTo(other: Boolean) =
    if (this) {
        if (other) {
            CHECKMARK
        } else {
            "$CHECKMARK<br/> endret til <br/>$XMARK"
        }
    } else {
        if (other) {
            "$XMARK<br/> endret til <br/>$CHECKMARK"
        } else {
            ""
        }
    }

fun List<Code>.changedTo(other: List<Code>, klassUrl: String?) : String =
    if (this == other)
        "Ingen endringer"
    else if (klassUrl != null)
        "[Vis endringer](${klassUrl}/endringer)"
    else
        ""
