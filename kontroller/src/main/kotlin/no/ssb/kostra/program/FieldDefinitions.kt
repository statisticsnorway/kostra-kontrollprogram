package no.ssb.kostra.program

open class FieldDefinitions {
    open val fieldDefinitions: List<FieldDefinition> = emptyList()

    val fieldLength: Int
        get() = fieldDefinitions.last().to

    val fieldDefinitionsByName: Map<String, FieldDefinition>
        get() = fieldDefinitions.associateBy { it.name }
}