package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Utils
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule010AvsluttedeMeklingerHvorBarnHarDeltatt : AbstractRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE010.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) =
        Utils.validateMatrix(context, fieldList, columns)
            .mapNotNull {
                val itemListSum = it.itemList.sumOf { item -> item.second }
                createValidationReportEntry(
                    messageText = "Summen (${it.sumItem.first}) med verdi (${it.sumItem.second}) " +
                            "er ulik summen ($itemListSum) av følgende liste (${it.itemList})",
                    lineNumbers = it.lineNumbers
                )
            }
            .takeIf { it.any() }

    companion object {
        const val columns = 6

        val fieldList = listOf(
            Familievern55ColumnNames.BARNDELT_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_TOT_TOT_COL_NAME,
        )
    }
}
