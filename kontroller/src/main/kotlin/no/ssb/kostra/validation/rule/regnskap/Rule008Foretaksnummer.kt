package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FORETAKSNR
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule008Foretaksnummer(
    val arguments: KotlinArguments
) : AbstractRecordRule("Kontroll 008 : Foretaksnummer", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord -> kostraRecord.getFieldAsString(FIELD_FORETAKSNR) != arguments.foretaknr }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig foretaksnummer '${kostraRecord.getFieldAsString(FIELD_FORETAKSNR)}'. Korrigér skjema til '${arguments.foretaknr}'",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }
        .ifEmpty { null }
}