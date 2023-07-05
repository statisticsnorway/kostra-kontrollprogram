package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.INTEGER_TYPE
import no.ssb.kostra.program.KostraRecord
import java.time.LocalDate
import kotlin.reflect.typeOf

@SuppressWarnings("all")
inline fun <reified T : Any?> KostraRecord.fieldAs(field: String, trim: Boolean = true): T = typeOf<T>().run {
    when (classifier) {
        Int::class -> if (isMarkedNullable) fieldAsInt(field) else fieldAsIntOrDefault(field)
        LocalDate::class -> if (isMarkedNullable) fieldAsLocalDate(field) else fieldAsLocalDate(field)!!
        String::class -> (if (isMarkedNullable) get(field).valueOrNull() else get(field))
            ?.let { if (trim) it.trim() else it }

        else -> throw IllegalArgumentException("fieldAs(): Unsupported type $classifier")
    }
} as T

fun KostraRecord.toRecordString(): String {
    val fieldDefinitions = fieldDefinitionByName.values.sortedBy { it.to }

    return if (fieldDefinitions.isEmpty()) ""
    else fieldDefinitions.fold(
        " ".repeat(fieldDefinitions.last().to)
    ) { recordString: String, fieldDefinition: FieldDefinition ->
        with(fieldDefinition) {
            val fieldLength = to - from + 1
            val value = fieldAsString(name)
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
        lineNumber = this.lineNumber,
        valuesByName = this.valuesByName.toMutableMap().plus(pair).toMap(),
        fieldDefinitionByName = this.fieldDefinitionByName
    )

fun String.toKostraRecord(
    index: Int,
    fieldDefinitions: List<FieldDefinition>
): KostraRecord {
    val recordString = this
    return KostraRecord(
        index,
        fieldDefinitions.associate { with(it) { name to recordString.substring(from - 1, to) } },
        fieldDefinitions.associate { with(it) { name to it } }
    )
}