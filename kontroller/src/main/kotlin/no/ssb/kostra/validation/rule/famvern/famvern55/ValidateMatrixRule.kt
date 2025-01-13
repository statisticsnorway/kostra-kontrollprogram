package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Famvern55Utils.validateMatrix
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

abstract class ValidateMatrixRule(
    ruleName: String,
    severity: Severity,
) : AbstractNoArgsRule<List<KostraRecord>>(
        ruleName = ruleName,
        severity = severity,
    ) {
    protected abstract val fieldList: List<String>
    protected abstract val columnSize: Int

    override fun validate(context: List<KostraRecord>) =
        validateMatrix(
            kostraRecordList = context,
            fieldList = fieldList,
            columnSize = columnSize,
        ).map {
            val itemListSum = it.itemList.sumOf { item -> item.second }
            createValidationReportEntry(
                messageText =
                    "Summen (${it.sumItem.first}) med verdi (${it.sumItem.second}) " +
                        "er ulik summen ($itemListSum) av følgende liste (${it.itemList})",
                lineNumbers = it.lineNumbers,
            )
        }.takeIf { it.any() }
}
