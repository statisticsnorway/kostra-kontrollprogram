package no.ssb.kostra.program


data class FieldDefinition(
    val number: Int = 0,
    val name: String,
    val dataType: String = INTEGER_TYPE,
    val from: Int = 0,
    val to: Int = 0,
    var codeList: List<Code> = emptyList(),
    var datePattern: String = "",
    val mandatory: Boolean = false,
    val size: Int = 1
) {
    init {
        if (dataType.equals(DATE_TYPE, ignoreCase = true) && datePattern.isBlank()) {
            datePattern = DATE8_PATTERN
        }
    }
}