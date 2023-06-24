package no.ssb.kostra.area

import no.ssb.kostra.program.FieldDefinition

abstract class AbstractFieldDefinitions {
    abstract val fieldDefinitions: List<FieldDefinition>

    fun getFieldLength(): Int = fieldDefinitions.last().to
}