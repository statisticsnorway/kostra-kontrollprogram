package no.ssb.kostra.program

data class FieldDescription(
    val name: String,
    val description: String? = null,
    val dataType: DataType = DataType.INTEGER_TYPE,
    val codeList: List<Code> = emptyList(),
    val datePattern: String = "",
    val mandatory: Boolean = false,
    val size: Int = 1
)
