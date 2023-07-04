package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control32KvalifiseringssumOverMaksimum : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KVALIFISERINGSSUM_OVER_MAKSIMUM_32.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.fieldAs<Int?>(KVP_STONAD_COL_NAME)
            ?.takeIf { it > STONAD_SUM_MAX }
            ?.let { belop ->
                createSingleReportEntryList(
                    "Kvalifiseringsstønaden ($belop) " +
                            "som deltakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                            "kontrollgrense på NOK $STONAD_SUM_MAX,-."
                )
            }

    companion object {
        const val STONAD_SUM_MAX = 600_000
    }
}