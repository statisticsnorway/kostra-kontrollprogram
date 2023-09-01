package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Famvern55Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule005AvsluttedeMeklingerEtterTidsbruk : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE005.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = validateMatrix(context, fieldList, NUM_COLS).map {
            val itemListSum = it.itemList.sumOf { item -> item.second }
            createValidationReportEntry(
                messageText = "Summen (${it.sumItem.first}) med verdi (${it.sumItem.second}) " +
                        "er ulik summen ($itemListSum) av følgende liste (${it.itemList})",
                lineNumbers = it.lineNumbers
            )
        }.takeIf { it.any() }

    companion object {
        const val NUM_COLS = 4

        val fieldList = listOf(
            MEKLING_SEP_1_COL_NAME,
            MEKLING_SEP_2_COL_NAME,
            MEKLING_SEP_3_COL_NAME,
            MEKLING_SEP_TOT_COL_NAME,
            MEKLING_SAM_1_COL_NAME,
            MEKLING_SAM_2_COL_NAME,
            MEKLING_SAM_3_COL_NAME,
            MEKLING_SAM_TOT_COL_NAME,
            MEKLING_SAK_1_COL_NAME,
            MEKLING_SAK_2_COL_NAME,
            MEKLING_SAK_3_COL_NAME,
            MEKLING_SAK_TOT_COL_NAME,
            MEKLING_TILB_1_COL_NAME,
            MEKLING_TILB_2_COL_NAME,
            MEKLING_TILB_3_COL_NAME,
            MEKLING_TILB_TOT_COL_NAME,
            MEKLING_FLY_1_COL_NAME,
            MEKLING_FLY_2_COL_NAME,
            MEKLING_FLY_3_COL_NAME,
            MEKLING_FLY_TOT_COL_NAME,
            MEKLING_TOT_1_COL_NAME,
            MEKLING_TOT_2_COL_NAME,
            MEKLING_TOT_3_COL_NAME,
            MEKLING_TOT_ALLE_COL_NAME,
        )
    }
}
