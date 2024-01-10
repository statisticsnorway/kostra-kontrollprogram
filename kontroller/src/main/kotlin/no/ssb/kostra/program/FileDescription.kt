package no.ssb.kostra.program

data class FileDescription(
    val title: String,
    val reportingYear: Int,
    val description: String,
    val fields: List<FieldDefinition>
)
