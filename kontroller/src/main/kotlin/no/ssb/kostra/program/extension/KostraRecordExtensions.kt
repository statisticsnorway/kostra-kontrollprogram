package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.INTEGER_TYPE
import no.ssb.kostra.program.KostraRecord

// CR NOTE: Only in use in tests
fun KostraRecord.toRecordString(): String = fieldDefinitionByName.values
    .sortedBy { it.to }
    .let { fieldDefinitions ->
        if (fieldDefinitions.isEmpty()) ""
        else {
            fieldDefinitions.fold(" ".repeat(fieldDefinitions.last().to)) { recordString, fieldDefinition ->
                with(fieldDefinition) {
                    val fieldLength = to - from + 1
                    val value = fieldAsString(name)
                    val stringValue =
                        if (dataType == INTEGER_TYPE) value.trim().padStart(fieldLength, ' ')
                        else value.padEnd(fieldLength, ' ')

                    recordString.replaceRange(from - 1, to, stringValue.substring(0, fieldLength))
                }
            }
        }
    }

fun KostraRecord.plus(pair: Pair<String, String>): KostraRecord = KostraRecord(
    lineNumber = this.lineNumber,
    valuesByName = this.valuesByName.toMutableMap().plus(pair).toMap(),
    fieldDefinitionByName = this.fieldDefinitionByName
)

fun KostraRecord.plus(map: Map<String, String>): KostraRecord = map.entries
    .fold(this) { kostraRecord, entry ->
        KostraRecord(
            lineNumber = kostraRecord.lineNumber,
            valuesByName = kostraRecord.valuesByName.toMutableMap().plus(entry.key to entry.value).toMap(),
            fieldDefinitionByName = kostraRecord.fieldDefinitionByName
        )
    }

fun String.toKostraRecord(
    index: Int,
    fieldDefinitions: List<FieldDefinition>
): KostraRecord = this.let { recordString ->
    KostraRecord(
        lineNumber = index,
        valuesByName = fieldDefinitions.associate { with(it) { name to recordString.substring(from - 1, to) } },
        fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
    )
}

// CR NOTE: Only in use in tests
fun KostraRecord.asList() = listOf(this)

// CR NOTE: Only in use in tests
fun Map<String, String>.toKostraRecord(
    lineNumber: Int,
    fieldDefinitions: List<FieldDefinition>
) = KostraRecord(
    lineNumber = lineNumber,
    valuesByName = this,
    fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
)

// CR NOTE: Only in use in tests
fun List<Map<String, String>>.toKostraRecords(
    fieldDefinitions: List<FieldDefinition>
) = this.mapIndexed { index, map ->
    KostraRecord(
        lineNumber = index + 1,
        valuesByName = map,
        fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
    )
}
