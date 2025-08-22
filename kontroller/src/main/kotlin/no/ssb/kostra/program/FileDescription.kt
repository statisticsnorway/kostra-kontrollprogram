package no.ssb.kostra.program

data class FileDescription(
    val id: String = "",
    val title: String = "File description",
    val reportingYear: Int = 0,
    val description: String = "Default file description",
    val fields: List<FieldDefinition> = emptyList()
)
