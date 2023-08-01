package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule030StonadssumMaksimum : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K030_STONADSSUMMAX.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filterNot {
            (it.fieldAsIntOrDefault(BIDRAG_COL_NAME) + it.fieldAsIntOrDefault(LAAN_COL_NAME)) < max
        }.takeIf {
            it.any()
        }?.map {
            val stonad = it.fieldAsIntOrDefault(BIDRAG_COL_NAME) + it.fieldAsIntOrDefault(LAAN_COL_NAME)
            createValidationReportEntry(
                "Det samlede stønadsbeløpet (summen ($stonad) " +
                        "av bidrag (${it[BIDRAG_COL_NAME]}) og lån (${it[LAAN_COL_NAME]})) som mottakeren " +
                        "har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                        "kontrollgrense på kr. (${max}),-."
            )
        }

    companion object {
        internal const val max = 600000
    }
}
