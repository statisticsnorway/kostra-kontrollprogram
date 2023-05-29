package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule012Art(
    val artList: List<String>
) : AbstractRecordRule("Kontroll 012 : Art", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? =
        if (artList.isEmpty()) null
        else context
            .filter { kostraRecord -> artList.none { it == kostraRecord.getFieldAsString(FIELD_ART) } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = """Fant ugyldig art '${kostraRecord.getFieldAsString(FIELD_ART)}'. 
                                Korrigér art til en av '${artList.joinToString(", ")}'""".trimMargin(),
                    lineNumbers = listOf(kostraRecord.index)
                )
            }
            .ifEmpty { null }
}