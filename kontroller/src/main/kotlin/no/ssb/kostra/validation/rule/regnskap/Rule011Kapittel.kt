package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule011Kapittel(
    val kapittelList: List<String>
) : AbstractRecordRule("Kontroll 011 : Kapittel", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? =
        if (kapittelList.isEmpty()) null
        else context
            .filter { kostraRecord -> kapittelList.none { it == kostraRecord.getFieldAsString(FIELD_KAPITTEL) } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = """Fant ugyldig kapittel '${kostraRecord.getFieldAsString(FIELD_KAPITTEL)}'. 
                                Korrigér kapittel til en av '${kapittelList.joinToString(", ")}'""".trimMargin(),
                    lineNumbers = listOf(kostraRecord.index)
                )
            }
            .ifEmpty { null }
}