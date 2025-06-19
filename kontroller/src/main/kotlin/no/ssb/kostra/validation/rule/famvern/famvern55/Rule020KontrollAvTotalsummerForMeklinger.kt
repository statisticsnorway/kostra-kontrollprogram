package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule020KontrollAvTotalsummerForMeklinger :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern55RuleId.FAMILIEVERN55_RULE020.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot {
                fieldList
                    .map { field ->
                        field to it.fieldAsIntOrDefault(field)
                    }.all { item ->
                        item.second == it.fieldAsIntOrDefault(fieldList.first())
                    }
            }.map {
                val fieldValueList =
                    fieldList.map { field -> field to it.fieldAsIntOrDefault(field) }
                createValidationReportEntry(
                    messageText = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: $fieldValueList",
                    lineNumbers = listOf(it.lineNumber),
                )
            }.ifEmpty { null }

    companion object {
        private val fieldList =
            listOf(
                Familievern55ColumnNames.BARN_TOT_COL_NAME,
                Familievern55ColumnNames.BEKYMR_TOT_COL_NAME,
                Familievern55ColumnNames.VOLD_TOT_COL_NAME,
            )
    }
}