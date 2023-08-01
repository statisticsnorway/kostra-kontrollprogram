package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_10_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_11_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_12_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_1_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_2_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_3_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_4_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_5_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_6_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_7_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_8_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_9_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule036BidragSum : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K036_BIDRAGSUM.title,
    Severity.WARNING
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .map { kostraRecord ->
            months.sumOf { kostraRecord.fieldAsIntOrDefault(it) } to kostraRecord.fieldAsIntOrDefault(BIDRAG_COL_NAME)
        }.filterNot {
            it.first == it.second
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Det er ikke fylt ut bidrag (${it.first}) fordelt på måneder eller sum stemmer ikke " +
                        "med sum bidrag (${it.second}) utbetalt i løpet av året."
            )
        }

    companion object {
        val months = listOf(
            BIDRAG_1_COL_NAME,
            BIDRAG_2_COL_NAME,
            BIDRAG_3_COL_NAME,
            BIDRAG_4_COL_NAME,
            BIDRAG_5_COL_NAME,
            BIDRAG_6_COL_NAME,
            BIDRAG_7_COL_NAME,
            BIDRAG_8_COL_NAME,
            BIDRAG_9_COL_NAME,
            BIDRAG_10_COL_NAME,
            BIDRAG_11_COL_NAME,
            BIDRAG_12_COL_NAME,
        )
    }
}
