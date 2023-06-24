package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.findByColumnName
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
        val values = fieldsToCheck.associateWith { context.getFieldAsString(it) }

        return when (context.getFieldAsString(KVP_MED_ASTONAD_COL_NAME)) {
            "1" -> {
                fieldsToCheck.filter {
                    fieldDefinitions.findByColumnName(it).codeIsMissing(context.getFieldAsString(it))
                }.takeIf { it.isNotEmpty() }?.let {
                    createSingleReportEntryList(
                        "Svaralternativer for feltet \"Har deltakeren i ${arguments.aargang} i løpet av " +
                                "perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte " +
                                "eller Husbankens bostøtte?\" har ugyldige koder. " +
                                "Feltet er obligatorisk å fylle ut. Det er mottatt støtte. $values"
                    )
                }
            }

            "2" -> {
                fieldsToCheck
                    .filter { context.getFieldAsString(it) !in setOf(" ", "0") }
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
        val fieldsToCheck = setOf(
            KVP_MED_KOMMBOS_COL_NAME,
            KVP_MED_HUSBANK_COL_NAME,
            KVP_MED_SOSHJ_ENGANG_COL_NAME,
            KVP_MED_SOSHJ_PGM_COL_NAME,
            KVP_MED_SOSHJ_SUP_COL_NAME
        )
    }
}