package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Famvern55Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule013AvsluttedeMeklingerOgBekymringsmeldinger : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE013.title,
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
        const val NUM_COLS = 3

        val fieldList = listOf(
            Familievern55ColumnNames.BEKYMR_SEP_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SEP_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAM_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAM_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAK_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAK_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TILB_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TILB_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_FLY_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_FLY_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_TOT_COL_NAME,
        )
    }
}
