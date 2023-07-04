package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.JA
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.NEI
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.valueOrNull
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control27MottattOkonomiskSosialhjelp : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.MOTTATT_OKONOMISK_SOSIALHJELP_27.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val values = fieldNamesToCheck.associateWith { context[it] }

        return when (context[KVP_MED_ASTONAD_COL_NAME]) {
            JA -> {
                fieldNamesToCheck
                    .mapNotNull { fieldName -> context[fieldName].valueOrNull()?.let { fieldName to it } }
                    .filter { (fieldName, fieldValue) ->
                        fieldDefinitions.byColumnName(fieldName).codeIsMissing(fieldValue)
                    }
                    .takeIf { it.isNotEmpty() }?.let {
                        createSingleReportEntryList(
                            "Svaralternativer for feltet \"Har deltakeren i ${arguments.aargang} i løpet av " +
                                    "perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte " +
                                    "eller Husbankens bostøtte?\" har ugyldige koder. " +
                                    "Feltet er obligatorisk å fylle ut. Det er mottatt støtte. $values."
                        )
                    }
            }

            NEI -> {
                fieldNamesToCheck
                    .filter { context[it].valueOrNull() != null }
                    .takeIf { it.isNotEmpty() }
                    ?.let {
                        createSingleReportEntryList(
                            "Svaralternativer for feltet \"Har deltakeren i ${arguments.aargang} i løpet " +
                                    "av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal " +
                                    "bostøtte eller Husbankens bostøtte?\" har ugyldige koder. " +
                                    "Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte. $values"
                        )
                    }
            }

            else -> null
        }
    }

    companion object {
        val fieldNamesToCheck = setOf(
            KVP_MED_KOMMBOS_COL_NAME,
            KVP_MED_HUSBANK_COL_NAME,
            KVP_MED_SOSHJ_ENGANG_COL_NAME,
            KVP_MED_SOSHJ_PGM_COL_NAME,
            KVP_MED_SOSHJ_SUP_COL_NAME
        )
    }
}