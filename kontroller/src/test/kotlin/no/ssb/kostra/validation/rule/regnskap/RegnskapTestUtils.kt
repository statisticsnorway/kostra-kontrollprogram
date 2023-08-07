package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord

object RegnskapTestUtils {
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