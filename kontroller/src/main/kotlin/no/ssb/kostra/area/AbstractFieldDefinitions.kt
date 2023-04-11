package no.ssb.kostra.area

import no.ssb.kostra.program.FieldDefinition

abstract class AbstractFieldDefinitions {
    open fun getFieldDefinitions(): List<FieldDefinition> = listOf()

    fun getFieldLength(): Int {
        return getFieldDefinitions().last().to
    }

}