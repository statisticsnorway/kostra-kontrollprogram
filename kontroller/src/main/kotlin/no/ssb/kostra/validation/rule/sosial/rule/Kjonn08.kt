package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.GENDER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeListToString
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.area.sosial.kvalifisering.getCodes
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Kjonn08 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KJONN_08.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(GENDER_COL_NAME)

        return context.getFieldAsTrimmedString(GENDER_COL_NAME)
            .takeIf { it.isEmpty() || it !in fieldDefinition.getCodes() }
            ?.let { gender ->
                createSingleReportEntryList(
                    "Korrigér kjønn. Fant '$gender', forventet én av ${fieldDefinition.codeListToString()}." +
                    " Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                )
            }
    }
}