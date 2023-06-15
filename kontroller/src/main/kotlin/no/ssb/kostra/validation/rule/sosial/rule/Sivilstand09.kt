package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MARITAL_STATUS_COL_NAME
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

class Sivilstand09 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.SIVILSTAND_09.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(MARITAL_STATUS_COL_NAME)

        return context.getFieldAsTrimmedString(MARITAL_STATUS_COL_NAME)
            .takeIf { it.isEmpty() || it !in fieldDefinition.getCodes() }
            ?.let { maritalStatus ->
                createSingleReportEntryList(
                    "Korrigér sivilstand. Fant '$maritalStatus, forventet én av ${fieldDefinition.codeListToString()}." +
                            " Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er " +
                            "ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                )
            }
    }
}