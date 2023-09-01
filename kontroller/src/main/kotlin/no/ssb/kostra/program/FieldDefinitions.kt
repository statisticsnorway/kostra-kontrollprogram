package no.ssb.kostra.program

interface FieldDefinitions {
    val fieldDefinitions: List<FieldDefinition>

    val fieldLength: Int get() = fieldDefinitions.last().to

    // CR NOTE: Only in use in tests
    val fieldDefinitionsByName: Map<String, FieldDefinition>
        get() = fieldDefinitions.associateBy { it.name }
}