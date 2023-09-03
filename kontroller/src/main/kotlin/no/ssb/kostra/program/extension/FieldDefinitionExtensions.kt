package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FieldDefinition

fun FieldDefinition.codeListToString() = this.codeList.map { it.toString() }

fun FieldDefinition.codeIsMissing(code: String) = this.codeList.none { it.code == code }

fun FieldDefinition.codeExists(code: String) = this.codeList.any { it.code == code }

fun Collection<FieldDefinition>.byColumnName(columnName: String) = this.first { it.name == columnName }

fun List<FieldDefinition>.buildFieldDefinitions() = mutableListOf<FieldDefinition>().also { fieldDefinitions ->
    ArrayDeque(this).also { dequeue ->
        var columnIndex = 1

        do {
            dequeue.removeFirst().also { current ->
                fieldDefinitions.add(
                    current.copy(
                        from = columnIndex,
                        to = columnIndex + current.size - 1
                    )
                )
                columnIndex += current.size
            }
        } while (dequeue.isNotEmpty())
    }
}
