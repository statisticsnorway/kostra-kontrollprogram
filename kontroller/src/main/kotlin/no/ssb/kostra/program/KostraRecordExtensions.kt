package no.ssb.kostra.program

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
        index = this.index,
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