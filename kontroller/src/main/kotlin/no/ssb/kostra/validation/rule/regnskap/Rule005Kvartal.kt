package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule

class Rule005Kvartal : AbstractRule<List<KostraRecord>>("Kontroll 005 : Kvartal", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments): List<ValidationReportEntry>? = context
        .filter { kostraRecord -> kostraRecord.getFieldAsString(FIELD_KVARTAL) != arguments.kvartal }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig kvartal '${kostraRecord.getFieldAsString(FIELD_KVARTAL)}'. Korrigér kvartal til '${arguments.kvartal}'",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }
        .ifEmpty { null }
}