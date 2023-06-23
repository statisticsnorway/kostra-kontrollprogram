package no.ssb.kostra.area

import no.ssb.kostra.program.FieldDefinition

abstract class AbstractFieldDefinitions {
    abstract fun getFieldDefinitions(): List<FieldDefinition>

    open fun getFieldLength(): Int = getFieldDefinitions().last().to
}