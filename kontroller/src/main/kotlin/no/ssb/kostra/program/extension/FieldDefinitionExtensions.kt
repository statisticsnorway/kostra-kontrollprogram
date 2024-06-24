package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDescription
import java.util.concurrent.atomic.AtomicInteger

fun FieldDefinition.codeListToString() = this.codeList.map { it.toString() }

fun FieldDefinition.codeIsMissing(code: String) = this.codeList.none { it.code == code }

fun FieldDefinition.codeExists(code: String) = this.codeList.any { it.code == code }

fun Collection<FieldDefinition>.byColumnName(columnName: String) = this.first { it.name == columnName }

fun List<FieldDefinition>.buildFieldDefinitions() =
    AtomicInteger(1)
        .let { columnIndex ->
            this.map {
                it.copy(from = columnIndex.getAndAdd(it.size))
            }
        }
