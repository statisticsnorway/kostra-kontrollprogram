package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule026MottattStotte : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.MOTTATT_STOTTE_26.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            fieldDefinitions.byColumnName(KVP_MED_ASTONAD_COL_NAME).codeIsMissing(it[KVP_MED_ASTONAD_COL_NAME])
        }.map {
            createValidationReportEntry(
                "Feltet for 'Har deltakeren i ${arguments.aargang} i løpet av perioden med " +
                        "kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, kommunal bostøtte eller " +
                        "Husbankens bostøtte?', er ikke utfylt eller feil kode (${it[KVP_MED_ASTONAD_COL_NAME]}) " +
                        "er benyttet. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
                individId = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }
}