package no.ssb.kostra.program

import no.ssb.kostra.program.DataType.*

data class FieldDefinition(
    val name: String,
    val dataType: DataType = INTEGER_TYPE,
    val from: Int = 0,
    val codeList: List<Code> = emptyList(),
    val datePattern: String = "",
    val mandatory: Boolean = false,
    val size: Int = 1
) {
    constructor(
        name: String,
        size: Int = 1,
        code: Code
    ) : this(name, STRING_TYPE, 0, listOf(code), "", false, size)

    constructor(
        name: String,
        size: Int = 1,
        mandatory: Boolean = false,
        codeListOverload: List<Code>
    ) : this(name, STRING_TYPE, 0, codeListOverload, "", mandatory, size)

    init {
        require(!(dataType == DATE_TYPE && datePattern.isBlank())) {
            "datePattern cannot be empty or blank when dataType is $DATE_TYPE"
        }
    }

    val to: Int get() = from + size - 1

    companion object {
        val yesNoCodeList = listOf(
            Code("1", "Ja"),
            Code("2", "Nei")
        )
    }
}