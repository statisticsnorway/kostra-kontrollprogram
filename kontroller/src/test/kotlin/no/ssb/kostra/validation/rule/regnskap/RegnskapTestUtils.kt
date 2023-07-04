package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord

object RegnskapTestUtils {

    fun KostraRecord.asList() = listOf(this)

    fun Map<String, String>.toKostraRecord(lineNumber: Int = 1) = regnskapRecordInTest(
        valuesByName = this,
        lineNumber = lineNumber
    )

    fun Collection<Map<String, String>>.toKostraRecords(): List<KostraRecord> = this.mapIndexed { index, valuesByName ->
        regnskapRecordInTest(
            valuesByName = valuesByName,
            lineNumber = index + 1
        )
    }

    fun regnskapRecordInTest(
        valuesByName: Map<String, String> = emptyMap(),
        lineNumber: Int = 1,
        fieldDefinitions: Collection<FieldDefinition> = RegnskapFieldDefinitions.fieldDefinitions
    ) = KostraRecord(
        lineNumber = lineNumber,
        valuesByName = valuesByName,
        fieldDefinitionByName = fieldDefinitions.associateBy { it.name }
    )
}