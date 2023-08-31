package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule006AvsluttedeMeklingerEtterDeltakere : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE006.title,
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
        const val NUM_COLS = 3

        val fieldList = listOf(
            Familievern55ColumnNames.SEP_BEGGE_COL_NAME,
            Familievern55ColumnNames.SEP_EN_COL_NAME,
            Familievern55ColumnNames.SEP_TOT_COL_NAME,
            Familievern55ColumnNames.SAM_BEGGE_COL_NAME,
            Familievern55ColumnNames.SAM_EN_COL_NAME,
            Familievern55ColumnNames.SAM_TOT_COL_NAME,
            Familievern55ColumnNames.SAK_BEGGE_COL_NAME,
            Familievern55ColumnNames.SAK_EN_COL_NAME,
            Familievern55ColumnNames.SAK_TOT_COL_NAME,
            Familievern55ColumnNames.TILB_BEGGE_COL_NAME,
            Familievern55ColumnNames.TILB_EN_COL_NAME,
            Familievern55ColumnNames.TILB_TOT_COL_NAME,
            Familievern55ColumnNames.FLY_BEGGE_COL_NAME,
            Familievern55ColumnNames.FLY_EN_COL_NAME,
            Familievern55ColumnNames.FLY_TOT_COL_NAME,
            Familievern55ColumnNames.BEGGE_TOT_COL_NAME,
            Familievern55ColumnNames.EN_TOT_COL_NAME,
            Familievern55ColumnNames.ENBEGGE_TOT_COL_NAME,
        )
    }
}
