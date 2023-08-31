package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule014KontrollAvTotalsummerForMeklinger : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE014.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context.filterNot {
        fieldList.map { field ->
            field to it.fieldAsIntOrDefault(field)
        }.all { item -> item.second == it.fieldAsIntOrDefault(fieldList.first()) }
    }.map {
        val fieldValueList = fieldList.map { field -> field to it.fieldAsIntOrDefault(field) }
        createValidationReportEntry(
            messageText = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: $fieldValueList",
            lineNumbers = listOf(it.lineNumber)
        )
    }.ifEmpty { null }

    companion object {
        val fieldList = listOf(
            Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME,
            Familievern55ColumnNames.ENBEGGE_TOT_COL_NAME,
            Familievern55ColumnNames.VENTETID_TOT_TOT_COL_NAME,
            Familievern55ColumnNames.VARIGHET_TOT_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_TOT_COL_NAME
        )
    }
}
