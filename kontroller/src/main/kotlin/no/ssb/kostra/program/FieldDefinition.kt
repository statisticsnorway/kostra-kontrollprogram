package no.ssb.kostra.program


data class FieldDefinition(
    val number: Int = 0,
    val name: String,
    val dataType: String = INTEGER_TYPE,
    val from: Int = 0,
    val codeList: List<Code> = emptyList(),
    val datePattern: String = "",
    val mandatory: Boolean = false,
    val size: Int = 1
) {
    init {
        if (dataType == DATE_TYPE && datePattern.isBlank()) {
            throw IllegalArgumentException("datePattern cannot be empty or blank when dataType is $DATE_TYPE")
        }
    }

    val to: Int get() = from + size - 1
}