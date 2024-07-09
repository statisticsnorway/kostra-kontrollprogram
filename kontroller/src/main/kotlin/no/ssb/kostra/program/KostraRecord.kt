package no.ssb.kostra.program

import java.nio.file.NoSuchFileException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

data class KostraRecord(
    val lineNumber: Int = 1,
    val valuesByName: Map<String, String> = emptyMap(),
    val fieldDefinitionByName: Map<String, FieldDefinition> = emptyMap()
) {
    operator fun get(field: String) = fieldAsString(field)

    @Throws(NoSuchFieldException::class)
    fun fieldAsString(field: String): String = valuesByName.getOrElse(field) {
        throw NoSuchFieldException("fieldAsString(): $field is missing")
    }

    fun fieldAsTrimmedString(field: String): String = fieldAsString(field).trim()

    fun fieldAsInt(field: String): Int? = try {
        fieldAsTrimmedString(field).toInt()
    } catch (thrown: NumberFormatException) {
        null
    }

    fun fieldAsIntOrDefault(
        field: String,
        defaultValue: Int = 0
    ): Int = try {
        fieldAsTrimmedString(field).toInt()
    } catch (thrown: NumberFormatException) {
        defaultValue
    }

    @Throws(IndexOutOfBoundsException::class)
    fun fieldAsLocalDate(field: String): LocalDate? {
        val definition = fieldDefinition(field)
        val pattern = definition.datePattern.trim()
        val value = fieldAsString(field)

        if (pattern.length != value.length)
            throw IndexOutOfBoundsException("fieldAsLocalDate(): value and datePattern have different lengths")

        return try {
            DateTimeFormatter.ofPattern(pattern).let { LocalDate.from(it.parse(value)) }
        } catch (e: DateTimeParseException) {
            null
        }
    }

    @Throws(NoSuchFieldException::class)
    internal fun fieldDefinition(name: String): FieldDefinition =
        fieldDefinitionByName.getOrElse(name) {
            throw NoSuchFieldException("fieldDefinitionByName(): $name is missing")
        }

    override fun toString(): String = "$valuesByName\n$fieldDefinitionByName"

    override fun equals(other: Any?): Boolean = when {
        other == null || other !is KostraRecord -> false
        this === other -> true
        else -> this.lineNumber == other.lineNumber && valuesByName == other.valuesByName
    }

    override fun hashCode() = Objects.hash(lineNumber, valuesByName)
}