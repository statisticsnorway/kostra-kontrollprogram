package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Familievern55FieldDefinitions
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord

object Familievern55TestUtils {
    fun familievernRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = " ".repeat(Familievern55FieldDefinitions.fieldDefinitions.last().to)
        .toKostraRecord(index = 1, Familievern55FieldDefinitions.fieldDefinitions)
        .plus(Familievern55ColumnNames.FYLKE_NR_COL_NAME to "~~")
        .plus(valuesByName)
}