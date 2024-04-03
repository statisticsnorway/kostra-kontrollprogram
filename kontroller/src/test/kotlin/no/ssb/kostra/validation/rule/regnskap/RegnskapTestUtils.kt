package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_AARGANG
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import java.time.Year

object RegnskapTestUtils {
    fun regnskapRecordInTest(
        valuesByName: Map<String, String> = emptyMap(), index: Int = 1
    ) = " ".repeat(fieldDefinitions.last().to)
        .toKostraRecord(index = index, fieldDefinitions)
        .plus(FIELD_AARGANG to (Year.now().value - 1).toString())
        .plus(FIELD_REGION to "1234  ")
        .plus(valuesByName)
}