package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule032KvalifiseringssumOverMaksimum : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.KVALIFISERINGSSUM_OVER_MAKSIMUM_32.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            STONAD_SUM_MAX < it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME)
        }.map {
            createValidationReportEntry(
                "Kvalifiseringsstønaden (${it[KVP_STONAD_COL_NAME]}) " +
                        "som deltakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                        "kontrollgrense på NOK $STONAD_SUM_MAX,-."
            )
        }.ifEmpty { null }

    companion object {
        const val STONAD_SUM_MAX = 600_000
    }
}