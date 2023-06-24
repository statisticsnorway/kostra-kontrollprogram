package no.ssb.kostra.area

import no.ssb.kostra.program.FieldDefinition

abstract class AbstractFieldDefinitions {
    abstract val fieldDefinitions: List<FieldDefinition>

    val fieldLength: Int
        get() = fieldDefinitions.last().to
}