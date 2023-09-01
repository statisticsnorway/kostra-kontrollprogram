package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.area.famvern.famvern55.Famvern55Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE008.title,
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
            Familievern55ColumnNames.FORHOLD_MEKLER_COL_NAME,
            Familievern55ColumnNames.FORHOLD_KLIENT_COL_NAME,
            Familievern55ColumnNames.FORHOLD_TOT_COL_NAME,
        )
    }
}
