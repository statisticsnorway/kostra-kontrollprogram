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

fun Pair<FileDescription, FileDescription>.toChangeLogMarkdown(): String =
    this.let { (a, b) ->
        buildString {
            val fieldsA = a.fields.associateBy { it.name }
            val fieldsB = b.fields.associateBy { it.name }
            appendLine("# Endringslogg: ${a.title}, fra  ${a.reportingYear} til ${b.reportingYear}")
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

fun Pair<Map<String, FieldDefinition>, Map<String, FieldDefinition>>.toAddedChangeLogMarkdown(): String =
    this
        .let { (a, b) ->
            buildString {
                (b.keys - a.keys)
                    .takeIf { addedFields -> addedFields.isNotEmpty() }
                    ?.let { addedFields ->
                        appendLine()
                        appendLine("## Lagt til")
                        append(header())
                        append(
                            addedFields.mapNotNull { b[it] }.toMarkDownTable()
                        )
                    }
            }
        }

fun Pair<Map<String, FieldDefinition>, Map<String, FieldDefinition>>.toRemovedChangeLogMarkdown(): String =
    this
        .let { (a, b) ->
            buildString {
                (a.keys - b.keys)
                    .takeIf { removedFields -> removedFields.isNotEmpty() }
                    ?.let { removedFields ->
                        appendLine()
                        appendLine("## Fjernet")
                        append(header())
                        append(
                            removedFields.mapNotNull { a[it] }.toMarkDownTable()
                        )
                    }
            }
        }

fun Pair<Map<String, FieldDefinition>, Map<String, FieldDefinition>>.toChangedLogMarkdown(): String =
    this
        .let { (a, b) ->
            buildString {
                (a.keys.intersect(b.keys))
                    .map { a[it]!! to b[it]!! }
                    .filter { (a, b) -> a != b }
                    .takeIf { changedFields -> changedFields.isNotEmpty() }
                    ?.let { changedFields ->
                        appendLine()
                        appendLine("## Endret")
                        append(header())

                        changedFields.forEach { (a, b) ->
                            val name = a.name
                            val description =
                                (a.description ?: "").changedTo(
                                    b.description ?: ""
                                )
                            val size =
                                a.size.toString().changedTo(b.size.toString())
                            val fromTo =
                                "${a.from}-${a.to}".changedTo("${b.from}-${b.to}")
                            val dataType =
                                "${a.dataType}".changedTo("${b.dataType}")
                            val mandatory = a.mandatory.changedTo(b.mandatory)
                            val datePattern =
                                a.datePattern.changedTo(b.datePattern)
                            val codes = (a.codeList to a.codelistSource)
                                .changedTo(b.codeList to b.codelistSource)

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

fun Pair<List<Code>, String?>.changedTo(other: Pair<List<Code>, String?>): String =
    this.let { (aCodelist, aKlassUrl) ->
        other.let { (bCodelist, bKlassUrl) ->
            if (aCodelist == bCodelist) {
                "Ingen endringer"
            } else {
                buildString {
                    if (!aKlassUrl.isNullOrBlank() && !bKlassUrl.isNullOrBlank() && aKlassUrl == bKlassUrl) {
                        append("[Vis endringer i Klass](${bKlassUrl}/endringer)<br/><br/>")
                    }

                    val aMapped = aCodelist.associateBy { it.code }
                    val bMapped = bCodelist.associateBy { it.code }

                    (aMapped.keys - bMapped.keys)
                        .takeIf { removedCodes -> removedCodes.isNotEmpty() }
                        ?.map { removedCode -> aMapped[removedCode]!! }
                        ?.let { removedCodes ->
                            val removed = removedCodes
                                .joinToString("<br/>") { "`" + it.code + "`: " + it.value }
                            append("Fjernet:<br/>$removed<br/><br/>")
                        }

                    (bMapped.keys - aMapped.keys)
                        .takeIf { addedCodes -> addedCodes.isNotEmpty() }
                        ?.map { addedCodes -> bMapped[addedCodes]!! }
                        ?.let { addedCodes ->
                            val added = addedCodes
                                .joinToString("<br/>") { "`" + it.code + "`: " + it.value }
                            append("Lagt til:<br/>$added<br/><br/>")
                        }

                    (aMapped.keys.intersect(bMapped.keys))
                        .map { aMapped[it]!! to bMapped[it]!! }
                        .filter { (aCode, bCode) -> aCode != bCode }
                        .takeIf { changedCodes -> changedCodes.isNotEmpty() }
                        ?.let { changedCodes ->
                            val changed = changedCodes
                                .joinToString("<br/>") { (from, to) ->
                                    "`${from.code}` : ${from.value}<br/>endret til ${to.value}"
                                }
                            append("Endret:<br/>$changed<br/>")
                        }
                }
            }
        }
    }
