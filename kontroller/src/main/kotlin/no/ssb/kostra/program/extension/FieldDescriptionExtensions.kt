package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDescription
import java.util.concurrent.atomic.AtomicInteger

fun List<FieldDescription>.buildFieldDefinitions(): List<FieldDefinition> =
    AtomicInteger(1)
        .let { columnIndex ->
            this.map {
                it.toFieldDefinition(from = columnIndex.getAndAdd(it.size))
            }
        }


fun FieldDescription.toFieldDefinition(from: Int): FieldDefinition =
    FieldDefinition(
        name = this.name,
        description = this.description,
        dataType = this.dataType,
        from = from,
        codeList = this.codeList,
        datePattern = this.datePattern,
        mandatory = this.mandatory,
        size = this.size,
    )
