package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeIsMissing
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control19KvalifiseringsprogramIAnnenKommune : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_19.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KVP_KOMM_COL_NAME)
        val value = context.getFieldAsString(KVP_KOMM_COL_NAME)

        return if (fieldDefinition.codeIsMissing(value)) {
            createSingleReportEntryList(
                "Feltet for 'Kommer deltakeren fra kvalifiseringsprogram i annen kommune?' er ikke fylt ut, " +
                        "eller feil kode er benyttet ($value). Feltet er obligatorisk å fylle ut."
            )
        } else null
    }
}