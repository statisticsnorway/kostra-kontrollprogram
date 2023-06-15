package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control26MottattStotte : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.MOTTATT_STOTTE_26.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KVP_MED_ASTONAD_COL_NAME)
        val value = context.getFieldAsString(KVP_MED_ASTONAD_COL_NAME)

        return createSingleReportEntryList(
            "Feltet for 'Har deltakeren i ${arguments.aargang} i løpet av perioden med " +
                    "kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, kommunal bostøtte eller Husbankens " +
                    "bostøtte?', er ikke utfylt eller feil kode ($value) er benyttet. Feltet er obligatorisk å fylle ut."
        )
    }
}