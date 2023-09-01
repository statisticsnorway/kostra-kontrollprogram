package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Famvern55Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule007AvsluttedeMeklingerEtterTidsbruk : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE007.title,
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
        const val NUM_COLS = 5

        val fieldList = listOf(
            Familievern55ColumnNames.VENTETID_SEP_1_COL_NAME,
            Familievern55ColumnNames.VENTETID_SEP_2_COL_NAME,
            Familievern55ColumnNames.VENTETID_SEP_3_COL_NAME,
            Familievern55ColumnNames.VENTETID_SEP_4_COL_NAME,
            Familievern55ColumnNames.VENTETID_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAM_1_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAM_2_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAM_3_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAM_4_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAK_1_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAK_2_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAK_3_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAK_4_COL_NAME,
            Familievern55ColumnNames.VENTETID_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.VENTETID_TILB_1_COL_NAME,
            Familievern55ColumnNames.VENTETID_TILB_2_COL_NAME,
            Familievern55ColumnNames.VENTETID_TILB_3_COL_NAME,
            Familievern55ColumnNames.VENTETID_TILB_4_COL_NAME,
            Familievern55ColumnNames.VENTETID_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.VENTETID_FLY_1_COL_NAME,
            Familievern55ColumnNames.VENTETID_FLY_2_COL_NAME,
            Familievern55ColumnNames.VENTETID_FLY_3_COL_NAME,
            Familievern55ColumnNames.VENTETID_FLY_4_COL_NAME,
            Familievern55ColumnNames.VENTETID_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.VENTETID_TOT_1_COL_NAME,
            Familievern55ColumnNames.VENTETID_TOT_2_COL_NAME,
            Familievern55ColumnNames.VENTETID_TOT_3_COL_NAME,
            Familievern55ColumnNames.VENTETID_TOT_4_COL_NAME,
            Familievern55ColumnNames.VENTETID_TOT_TOT_COL_NAME
        )
    }
}
