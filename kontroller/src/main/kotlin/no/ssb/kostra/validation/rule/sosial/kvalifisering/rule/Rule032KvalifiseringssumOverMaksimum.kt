package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule032KvalifiseringssumOverMaksimum : AbstractNoArgsRule<List<KostraRecord>>(
    KvalifiseringRuleId.KVALIFISERINGSSUM_OVER_MAKSIMUM_32.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { STONAD_SUM_MAX < it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME) }
        .map {
            createValidationReportEntry(
                "Kvalifiseringsstønaden (${it[KVP_STONAD_COL_NAME]}) " +
                        "som deltakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                        "kontrollgrense på NOK $STONAD_SUM_MAX,-."
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        internal const val STONAD_SUM_MAX = 600_000
    }
}