package no.ssb.kostra.program

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Objects

data class KostraRecord(
    val lineNumber: Int = 1,
    val valuesByName: Map<String, String> = emptyMap(),
    val fieldDefinitionByName: Map<String, FieldDefinition> = emptyMap()
) {
    operator fun get(field: String) = fieldAsString(field)

    inline fun <reified T:Any?> fieldAs(field: String, trim: Boolean = true): T = when (T::class) {
        Int::class -> when {
            null is T -> fieldAsInt(field) as T
            else -> fieldAsIntOrDefault(field) as T
        }

        LocalDate::class -> when {
            null is T -> fieldAsLocalDate(field) as T
            else -> fieldAsLocalDate(field)!! as T
        }

        else -> when(trim){
            true -> fieldAsTrimmedString(field) as T
            else -> fieldAsString(field) as T
        }
    }

    fun fieldAsString(field: String): String = valuesByName.getOrElse(field) {
        throw NoSuchFieldException("getFieldAsString(): $field is missing")
    }

    fun fieldAsTrimmedString(field: String): String = fieldAsString(field).trim { it <= ' ' }

    fun fieldAsInt(field: String): Int? = fieldAsTrimmedString(field).takeIf { it.isNotEmpty() }?.toInt()

    fun fieldAsIntOrDefault(
        field: String,
        defaultValue: Int = 0
    ): Int = try {
        fieldAsTrimmedString(field).toInt()
    } catch (thrown: NumberFormatException) {
        defaultValue
    }

    fun fieldAsLocalDate(field: String): LocalDate? {
        val definition = fieldDefinition(field)
        val pattern = definition.datePattern.trim()
        val value = fieldAsString(field)

        if (pattern.length != value.length)
            throw IndexOutOfBoundsException("getFieldAsLocalDate(): value and datePattern have different lengths")

        return try {
            DateTimeFormatter.ofPattern(pattern).let { LocalDate.from(it.parse(value)) }
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun fieldDefinition(name: String): FieldDefinition =
        fieldDefinitionByName.getOrElse(name) {
            throw NoSuchFieldException("getFieldDefinitionByName(): $name is missing")
        }

    override fun toString(): String = "$valuesByName\n$fieldDefinitionByName"

    override fun equals(other: Any?): Boolean = when {
        other == null || other !is KostraRecord -> false
        this === other -> true
        else -> this.lineNumber == other.lineNumber && valuesByName == other.valuesByName
    }

    override fun hashCode() = Objects.hash(lineNumber, valuesByName)
}