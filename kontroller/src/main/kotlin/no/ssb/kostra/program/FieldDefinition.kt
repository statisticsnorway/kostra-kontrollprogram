package no.ssb.kostra.program

import no.ssb.kostra.program.DataType.DATE_TYPE
import no.ssb.kostra.program.DataType.INTEGER_TYPE

data class FieldDefinition(
    val number: Int = 0,
    val name: String,
    val dataType: DataType = INTEGER_TYPE,
    val from: Int = 0,
    val codeList: List<Code> = emptyList(),
    val datePattern: String = "",
    val mandatory: Boolean = false,
    val size: Int = 1
) {
    init {
        require(!(dataType == DATE_TYPE && datePattern.isBlank())) {
            "datePattern cannot be empty or blank when dataType is $DATE_TYPE"
        }
    }

    val to: Int get() = from + size - 1
}