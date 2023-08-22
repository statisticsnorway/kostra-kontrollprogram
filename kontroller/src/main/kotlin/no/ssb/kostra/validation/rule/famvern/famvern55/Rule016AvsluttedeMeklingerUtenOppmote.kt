package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule016AvsluttedeMeklingerUtenOppmote : AbstractRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE016.title,
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
        const val NUM_COLS = 6

        val fieldList = listOf(
            Familievern55ColumnNames.UTEN_OPPM_1_COL_NAME,
            Familievern55ColumnNames.UTEN_OPPM_2_COL_NAME,
            Familievern55ColumnNames.UTEN_OPPM_3_COL_NAME,
            Familievern55ColumnNames.UTEN_OPPM_4_COL_NAME,
            Familievern55ColumnNames.UTEN_OPPM_5_COL_NAME,
            Familievern55ColumnNames.UTEN_OPPM_TOT_COL_NAME,
        )
    }
}
