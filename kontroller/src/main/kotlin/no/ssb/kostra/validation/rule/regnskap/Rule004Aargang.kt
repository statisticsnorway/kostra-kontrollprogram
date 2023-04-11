package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_AARGANG
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule004Aargang(
    val arguments: Arguments
) : AbstractRecordRule("Kontroll 004 : Årgang", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord -> kostraRecord.getFieldAsString(FIELD_AARGANG) != arguments.aargang }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig aargang '${kostraRecord.getFieldAsString(FIELD_AARGANG)}'. Korrigér aargang til '${arguments.aargang}'",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}