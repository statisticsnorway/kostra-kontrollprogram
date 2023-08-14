package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.s52a.Familievern52aFieldDefinitions
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord

object Familievern52aTestUtils {
    fun familievernRecordInTest(
        valuesByName: Map<String, String> = emptyMap()
    ) = " ".repeat(Familievern52aFieldDefinitions.fieldDefinitions.last().to)
        .toKostraRecord(index = 1, Familievern52aFieldDefinitions.fieldDefinitions)
        .plus(valuesByName)
}