package no.ssb.kostra.program

data class FileDescription(
    val title: String = "",
    val reportingYear: Int = 0,
    val description: String = "",
    val fields: List<FieldDefinition> = emptyList()
)
