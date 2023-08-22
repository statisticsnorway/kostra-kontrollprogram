package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule012AntallAvsluttedeMeklingerSkriftligAvtaleEtterResultat : AbstractRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE012.title,
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
            Familievern55ColumnNames.AVTALE_SEP_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_SEP_2_COL_NAME,
            Familievern55ColumnNames.AVTALE_SEP_3_COL_NAME,
            Familievern55ColumnNames.AVTALE_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAM_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAM_2_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAM_3_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAK_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAK_2_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAK_3_COL_NAME,
            Familievern55ColumnNames.AVTALE_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.AVTALE_TILB_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_TILB_2_COL_NAME,
            Familievern55ColumnNames.AVTALE_TILB_3_COL_NAME,
            Familievern55ColumnNames.AVTALE_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.AVTALE_FLY_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_FLY_2_COL_NAME,
            Familievern55ColumnNames.AVTALE_FLY_3_COL_NAME,
            Familievern55ColumnNames.AVTALE_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.AVTALE_TOT_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_TOT_2_COL_NAME,
            Familievern55ColumnNames.AVTALE_TOT_3_COL_NAME,
            Familievern55ColumnNames.AVTALE_TOT_TOT_COL_NAME,
        )
    }
}
