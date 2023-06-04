package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule006Region(
    val arguments: KotlinArguments
) : AbstractRecordRule("Kontroll 006 : Region", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord -> kostraRecord.getFieldAsString(FIELD_REGION) != arguments.region }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig region '${kostraRecord.getFieldAsString(FIELD_REGION)}'. Korrigér region til '${arguments.region}'",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}