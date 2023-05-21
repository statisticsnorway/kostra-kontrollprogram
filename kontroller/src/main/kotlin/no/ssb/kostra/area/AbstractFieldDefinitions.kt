package no.ssb.kostra.area

import no.ssb.kostra.program.FieldDefinition

abstract class AbstractFieldDefinitions {
    open fun getFieldDefinitions(): List<FieldDefinition> = emptyList()

    fun getFieldLength(): Int = getFieldDefinitions().last().to
}