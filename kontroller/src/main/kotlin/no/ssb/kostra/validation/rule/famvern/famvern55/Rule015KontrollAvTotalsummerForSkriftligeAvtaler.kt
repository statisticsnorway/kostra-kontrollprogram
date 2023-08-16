package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule015KontrollAvTotalsummerForSkriftligeAvtaler : AbstractRule<List<KostraRecord>>(
    Familievern55RuleId.FAMILIEVERN55_RULE015.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        fieldList.map { field ->
            it.fieldAsIntOrDefault(field)
        }.all { value ->
            value == it.fieldAsIntOrDefault(fieldList.first())
        }
    }.map {
        val fieldValueList = fieldList.map { field -> field to it.fieldAsIntOrDefault(field) }
        createValidationReportEntry(
            messageText = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: $fieldValueList",
            lineNumbers = listOf(it.lineNumber)
        )
    }.ifEmpty { null }

    companion object {
        val fieldList = listOf(
            Familievern55ColumnNames.RESULT_TOT_1_COL_NAME,
            Familievern55ColumnNames.AVTALE_TOT_TOT_COL_NAME,
        )
    }
}
