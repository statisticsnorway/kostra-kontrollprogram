package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_10_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_11_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_12_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_1_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_2_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_3_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_4_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_5_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_6_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_7_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_8_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_9_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule037LaanSum : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K037_LAANSUM.title,
    Severity.WARNING
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .map { kostraRecord ->
            months.sumOf { kostraRecord.fieldAsIntOrDefault(it) } to kostraRecord.fieldAsIntOrDefault(LAAN_COL_NAME)
        }.filterNot {
            it.first == it.second
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Det er ikke fylt ut lån (${it.first}) fordelt på måneder eller sum stemmer ikke " +
                        "med sum lån (${it.second}) utbetalt i løpet av året."
            )
        }

    companion object {
        val months = listOf(
            LAAN_1_COL_NAME,
            LAAN_2_COL_NAME,
            LAAN_3_COL_NAME,
            LAAN_4_COL_NAME,
            LAAN_5_COL_NAME,
            LAAN_6_COL_NAME,
            LAAN_7_COL_NAME,
            LAAN_8_COL_NAME,
            LAAN_9_COL_NAME,
            LAAN_10_COL_NAME,
            LAAN_11_COL_NAME,
            LAAN_12_COL_NAME,
        )
    }
}