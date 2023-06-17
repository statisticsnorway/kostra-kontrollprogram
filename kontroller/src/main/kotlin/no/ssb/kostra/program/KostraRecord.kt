package no.ssb.kostra.program

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Objects

data class KostraRecord(
    val index: Int = 0,
    val valuesByName: Map<String, String> = emptyMap(),
    val fieldDefinitionByName: Map<String, FieldDefinition> = emptyMap()
) {
    fun getFieldAsString(field: String): String = valuesByName.getOrElse(field) {
        throw NoSuchFieldException("getFieldAsString(): $field is missing")
    }

    fun getFieldAsTrimmedString(field: String): String = getFieldAsString(field).trim { it <= ' ' }

    fun getFieldAsInteger(field: String): Int? = getFieldAsTrimmedString(field).takeIf { it.isNotEmpty() }?.toInt()

    fun getFieldAsIntegerDefaultEquals0(
        field: String,
        defaultValue: Int = 0
    ): Int = try {
        getFieldAsTrimmedString(field).toInt()
    } catch (e: NumberFormatException) {
        defaultValue
    }

    fun getFieldDefinitionByName(name: String): FieldDefinition =
        fieldDefinitionByName.getOrElse(name) {
            throw NoSuchFieldException("getFieldDefinitionByName(): $name is missing")
        }

    fun getFieldAsLocalDate(field: String): LocalDate? {
        val definition = getFieldDefinitionByName(field)
        val pattern = definition.datePattern.trim()
        val value = getFieldAsString(field)

        if (pattern.length != value.length)
            throw IndexOutOfBoundsException("getFieldAsLocalDate(): value and datePattern have different lengths")

        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalDate.from(formatter.parse(value))
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun getFieldAsList(field: String) = getFieldAsString(field).split(",")

    override fun toString(): String = "$valuesByName\n$fieldDefinitionByName"

    override fun equals(other: Any?): Boolean = when {
        other == null || other !is KostraRecord -> false
        this === other -> true
        else -> this.index == other.index && valuesByName == other.valuesByName
    }

    override fun hashCode() = Objects.hash(index, valuesByName)
}