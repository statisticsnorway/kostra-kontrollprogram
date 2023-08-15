package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bFieldDefinitions
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord

object Familievern52bTestUtils {
    fun familievernRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = " ".repeat(Familievern52bFieldDefinitions.fieldDefinitions.last().to)
        .toKostraRecord(index = 1, Familievern52bFieldDefinitions.fieldDefinitions)
        .plus(KONTOR_NR_B_COL_NAME to "~k~")
        .plus(GRUPPE_NR_B_COL_NAME to "~gruppe~")
        .plus(valuesByName)
}