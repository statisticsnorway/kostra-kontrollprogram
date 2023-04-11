package no.ssb.kostra.program

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

data class KostraRecord(
    val index: Int = 0,
    val valuesByName: Map<String, String> = emptyMap(),
    val fieldDefinitionByName: Map<String, FieldDefinition> = emptyMap()
) {
    fun getFieldAsString(field: String) =
        if (!valuesByName.containsKey(field))
            throw NoSuchFieldException("getFieldAsString(): $field is missing")
        else valuesByName.getOrDefault(field, "")

    fun getFieldAsTrimmedString(field: String) = getFieldAsString(field).trim { it <= ' ' }

    fun getFieldAsInteger(field: String) = try {
        getFieldAsTrimmedString(field).toInt()
    } catch (e: Exception) {
        null
    }

    fun getFieldAsIntegerDefaultEquals0(field: String) = try {
        getFieldAsTrimmedString(field).toInt()
    } catch (e: Exception) {
        0
    }

    fun getFieldDefinitionByName(name: String): FieldDefinition? {
        if (!fieldDefinitionByName.containsKey(name))
            throw NoSuchFieldException("getFieldDefinitionByName(): $name is missing")

        return fieldDefinitionByName[name]
    }

    fun getFieldAsLocalDate(field: String): LocalDate? {
        val definition = getFieldDefinitionByName(field)
        val pattern = definition?.datePattern?.trim() ?: ""
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

    override fun toString(): String {
        return "$valuesByName\n$fieldDefinitionByName"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        val record1 = other as KostraRecord
        return (index == record1.index) && (valuesByName == record1.valuesByName)
    }

    override fun hashCode() = Objects.hash(index, valuesByName)
}

fun KostraRecord.toRecordString(): String {
    val fieldDefinitions = fieldDefinitionByName.values.sortedBy { it.to }

    return if (fieldDefinitions.isEmpty()) ""
    else fieldDefinitions.fold(
        " ".repeat(fieldDefinitions.last().to)
    ) { recordString: String, fieldDefinition: FieldDefinition ->
        with(fieldDefinition) {
            val fieldLength = to - from + 1
            val value = getFieldAsString(name)
            val stringValue =
                if (dataType == INTEGER_TYPE)
                    value.trim().padStart(fieldLength, ' ')
                else
                    value.padEnd(fieldLength, ' ')

            recordString.replaceRange(from - 1, to, stringValue)
        }
    }
}

fun KostraRecord.plus(pair: Pair<String, String>): KostraRecord =
    KostraRecord(
        this.index,
        this.valuesByName.toMutableMap().plus(pair).toMap(),
        this.fieldDefinitionByName
    )


fun String.toKostraRecord(index: Int, fieldDefinitions: List<FieldDefinition>): KostraRecord {
    val recordString = this
    return KostraRecord(
        index,
        fieldDefinitions.associate { with(it) { name to recordString.substring(from - 1, to) } },
        fieldDefinitions.associate { with(it) { name to it } }
    )
}