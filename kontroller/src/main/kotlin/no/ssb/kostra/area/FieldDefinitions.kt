package no.ssb.kostra.area

import no.ssb.kostra.program.FieldDefinition

interface FieldDefinitions {
    val fieldDefinitions: List<FieldDefinition>

    val fieldLength: Int
        get() = fieldDefinitions.last().to
}