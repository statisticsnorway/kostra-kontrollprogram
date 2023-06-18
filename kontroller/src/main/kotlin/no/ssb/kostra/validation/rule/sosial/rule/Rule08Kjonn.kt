package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeIsMissing
import no.ssb.kostra.area.sosial.kvalifisering.codeListToString
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule08Kjonn : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KJONN_08.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KJONN_COL_NAME)

        return context.getFieldAsTrimmedString(KJONN_COL_NAME)
            .takeIf { fieldDefinition.codeIsMissing(it) }
            ?.let { gender ->
                createSingleReportEntryList(
                    "Korrigér kjønn. Fant '$gender', forventet én av ${fieldDefinition.codeListToString()}." +
                    " Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                )
            }
    }
}