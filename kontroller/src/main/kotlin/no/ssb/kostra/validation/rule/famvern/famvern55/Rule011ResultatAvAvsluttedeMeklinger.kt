package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule011ResultatAvAvsluttedeMeklinger : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE011.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) =
        validateMatrix(context, fieldList, NUM_COLS).map {
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
            Familievern55ColumnNames.RESULT_SEP_1_COL_NAME,
            Familievern55ColumnNames.RESULT_SEP_2_COL_NAME,
            Familievern55ColumnNames.RESULT_SEP_3_COL_NAME,
            Familievern55ColumnNames.RESULT_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_1_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_2_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_3_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_1_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_2_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_3_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_1_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_2_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_3_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_1_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_2_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_3_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_1_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_2_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_3_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_TOT_COL_NAME,
        )
    }
}
