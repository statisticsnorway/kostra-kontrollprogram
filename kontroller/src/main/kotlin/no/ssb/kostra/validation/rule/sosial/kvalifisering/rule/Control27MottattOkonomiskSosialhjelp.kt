package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control27MottattOkonomiskSosialhjelp : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.MOTTATT_OKONOMISK_SOSIALHJELP_27.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = KvalifiseringFieldDefinitions.fieldDefinitions.findByColumnName(KVP_MED_ASTONAD_COL_NAME)
        val value = context.getFieldAsString(KVP_MED_ASTONAD_COL_NAME)

        val fields = listOf(
            KVP_MED_KOMMBOS_COL_NAME,
            KVP_MED_HUSBANK_COL_NAME,
            KVP_MED_SOSHJ_ENGANG_COL_NAME,
            KVP_MED_SOSHJ_PGM_COL_NAME,
            KVP_MED_SOSHJ_SUP_COL_NAME
        );

        val values = fields.associateWith { context.getFieldAsString(it) }

        return createSingleReportEntryList(
            "Svaralternativer for feltet \"Har deltakeren i ${arguments.aargang} i løpet av perioden " +
                    "med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens " +
                    "bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er mottatt støtte. $values"
        )
    }
}