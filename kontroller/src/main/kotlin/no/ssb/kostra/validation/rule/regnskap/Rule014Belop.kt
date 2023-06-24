package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule014Belop : AbstractRecordRule("Kontroll 014 : Beløp", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            with(kostraRecord.getFieldAsString(FIELD_BELOP)) {
                split("\t".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray().size != 1
                        || !matches("^\\s*?-?\\d+$".toRegex())
            }
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig beløp '${kostraRecord.getFieldAsString(FIELD_BELOP)}'. Korrigér beløp",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }
        .ifEmpty { null }
}