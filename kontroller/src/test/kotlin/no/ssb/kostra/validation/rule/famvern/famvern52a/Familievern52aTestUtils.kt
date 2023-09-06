package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord

object Familievern52aTestUtils {
    fun familievernRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = " ".repeat(Familievern52aFieldDefinitions.fieldDefinitions.last().to)
        .toKostraRecord(index = 1, Familievern52aFieldDefinitions.fieldDefinitions)
        .plus(KONTOR_NR_A_COL_NAME to "~k~")
        .plus(JOURNAL_NR_A_COL_NAME to "~journal~")
        .plus(valuesByName)
}