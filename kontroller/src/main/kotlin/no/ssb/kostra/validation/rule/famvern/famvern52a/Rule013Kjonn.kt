package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.PRIMK_KJONN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule013Kjonn : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE013.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        fieldDefinitions.byColumnName(PRIMK_KJONN_A_COL_NAME).codeExists(it[PRIMK_KJONN_A_COL_NAME])
    }.map {
        val codeList =
            fieldDefinitions.byColumnName(PRIMK_KJONN_A_COL_NAME).codeList
        createValidationReportEntry(
            messageText = "Primærklientens kjønn er ikke fylt ut eller feil kode er benyttet. " +
                    "Fant '${it[PRIMK_KJONN_A_COL_NAME]}', forventet én av: $codeList. " +
                    "Feltet er obligatorisk å fylle ut.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}