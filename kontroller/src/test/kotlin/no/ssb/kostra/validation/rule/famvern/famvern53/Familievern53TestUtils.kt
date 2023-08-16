package no.ssb.kostra.validation.rule.famvern.famvern53

import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames
import no.ssb.kostra.area.famvern.famvern53.Familievern53FieldDefinitions
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord

object Familievern53TestUtils {
    fun familievernRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = " ".repeat(Familievern53FieldDefinitions.fieldDefinitions.last().to)
        .toKostraRecord(index = 1, Familievern53FieldDefinitions.fieldDefinitions)
        .plus(Familievern53ColumnNames.FYLKE_NR_COL_NAME to "~~")
        .plus(Familievern53ColumnNames.KONTORNR_COL_NAME to "~k~")
        .plus(valuesByName)
}