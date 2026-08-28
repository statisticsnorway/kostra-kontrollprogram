package gradletask.program

import com.fasterxml.jackson.annotation.JsonIgnore

data class FieldDefinition(
    val name: String,
    val description: String? = null,
    val from: Int = 0,
    val size: Int = 1,
    val dataType: DataType = DataType.INTEGER_TYPE,
    val mandatory: Boolean = false,
    val datePattern: String = "",
    val codelistSource: String? = null,
    val codeList: List<Code> = emptyList(),
) {
    init {
        require(!(dataType == DataType.DATE_TYPE && datePattern.isBlank())) {
            "datePattern cannot be empty or blank when dataType is ${DataType.DATE_TYPE}"
        }
    }

    @get:JsonIgnore
    val to: Int get() = from + size - 1
}