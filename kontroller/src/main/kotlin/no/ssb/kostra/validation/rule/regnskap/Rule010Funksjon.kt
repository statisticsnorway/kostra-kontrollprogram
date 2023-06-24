package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule010Funksjon(
    val funksjonList: List<String>
) : AbstractRecordRule("Kontroll 010 : Funksjon", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? =
        if (funksjonList.isEmpty()) null
        else context
            .filter { kostraRecord -> funksjonList.none { it == kostraRecord.getFieldAsString(FIELD_FUNKSJON) } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = """Fant ugyldig funksjon '${kostraRecord.getFieldAsString(FIELD_FUNKSJON)}'. 
                                Korrigér funksjon til en av '${funksjonList.joinToString(", ")}'""".trimMargin(),
                    lineNumbers = listOf(kostraRecord.lineNumber)
                )
            }
            .ifEmpty { null }
}