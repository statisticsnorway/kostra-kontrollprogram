package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule009AvsluttedeMeklingerEtterVarighet : AbstractRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE009.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) =
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
            Familievern55ColumnNames.VARIGHET_SEP_1_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SEP_2_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SEP_3_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAM_1_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAM_2_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAM_3_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAK_1_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAK_2_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAK_3_COL_NAME,
            Familievern55ColumnNames.VARIGHET_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TILB_1_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TILB_2_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TILB_3_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.VARIGHET_FLY_1_COL_NAME,
            Familievern55ColumnNames.VARIGHET_FLY_2_COL_NAME,
            Familievern55ColumnNames.VARIGHET_FLY_3_COL_NAME,
            Familievern55ColumnNames.VARIGHET_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TOT_1_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TOT_2_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TOT_3_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TOT_TOT_COL_NAME,
        )
    }
}
