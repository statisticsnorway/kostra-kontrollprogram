package no.ssb.kostra.program

import no.ssb.kostra.program.DataType.*

data class FieldDefinition(
    val name: String,
    val description: String? = "",
    val dataType: DataType = INTEGER_TYPE,
    val from: Int = 0,
    val codeList: List<Code> = emptyList(),
    val datePattern: String = "",
    val mandatory: Boolean = false,
    val size: Int = 1
) {
    constructor(
        name: String,
        description: String? = null,
        size: Int = 1,
        code: Code
    ) : this(
        name = name,
        description = description,
        dataType = STRING_TYPE,
        from = 0,
        codeList = listOf(code),
        datePattern = "",
        mandatory = false,
        size = size
    )

    constructor(
        name: String,
        description: String? = null,
        size: Int = 1,
        mandatory: Boolean = false,
        codes: List<Code>
    ) : this(
        name = name,
        description = description,
        dataType = STRING_TYPE,
        from = 0,
        codeList = codes,
        datePattern = "",
        mandatory = mandatory,
        size = size
    )

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