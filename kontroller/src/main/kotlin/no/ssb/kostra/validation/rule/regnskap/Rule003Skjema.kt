package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule003Skjema(
    val arguments: KotlinArguments
) : AbstractRecordRule("Kontroll 003 : Skjema", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord -> kostraRecord.getFieldAsString(FIELD_SKJEMA) != arguments.skjema }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig skjema '${kostraRecord.getFieldAsString(FIELD_SKJEMA)}'. Korrigér skjema til '${arguments.skjema}'",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}