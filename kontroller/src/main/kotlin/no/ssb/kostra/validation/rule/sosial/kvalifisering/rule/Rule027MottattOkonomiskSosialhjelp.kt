package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
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
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule027MottattOkonomiskSosialhjelp : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.MOTTATT_OKONOMISK_SOSIALHJELP_27.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            (it[KVP_MED_ASTONAD_COL_NAME] == JA && fieldNamesToCheck.all { fieldName ->
                fieldDefinitions.byColumnName(fieldName).codeIsMissing(it[fieldName])
            })
                    ||
                    (it[KVP_MED_ASTONAD_COL_NAME] == NEI && fieldNamesToCheck.any { fieldName ->
                        fieldDefinitions.byColumnName(fieldName).codeExists(it[fieldName])
                    })

        }.map {
            val values = fieldNamesToCheck.associateWith { fieldName -> it[fieldName] }
            when (it[KVP_MED_ASTONAD_COL_NAME]) {
                JA -> createValidationReportEntry(
                    "Svaralternativer for feltet \"Har deltakeren i ${arguments.aargang} i løpet av " +
                            "perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte " +
                            "eller Husbankens bostøtte?\" har ugyldige koder. " +
                            "Feltet er obligatorisk å fylle ut. Det er mottatt støtte. $values.",
                    lineNumbers = listOf(it.lineNumber)
                ).copy(
                    caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                    journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
                    individId = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME],
                )

                else -> createValidationReportEntry(
                    "Svaralternativer for feltet \"Har deltakeren i ${arguments.aargang} i løpet " +
                            "av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal " +
                            "bostøtte eller Husbankens bostøtte?\" har ugyldige koder. " +
                            "Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte. $values",
                    lineNumbers = listOf(it.lineNumber)
                ).copy(
                    caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                    journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
                    individId = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME],
                )
            }
        }.ifEmpty { null }

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