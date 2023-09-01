package no.ssb.kostra.program


data class FieldDefinition(
    val number: Int = 0,
    val name: String = "",
    val dataType: String = STRING_TYPE,
    val viewType: String = INPUTBOX_VIEWTYPE,
    val from: Int = 0,
    val to: Int = 0,
    var codeList: List<Code> = emptyList(),
    var datePattern: String = "",
    val mandatory: Boolean = false
) {
    init {
        if (viewType.equals(CHECKBOX_VIEWTYPE, ignoreCase = true) && codeList.isEmpty()) {
            codeList = listOf(
                Code(" ", "uninitialzed"),
                Code("0", "unchecked"),
                Code("1", "checked")
            )
        }

        if (dataType.equals(DATE_TYPE, ignoreCase = true) && datePattern.isBlank()) {
            datePattern = DATE8_PATTERN
        }
    }

    val length: Int get() = to + 1 - from
}