package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.PRIMK_SIVILS_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule015Samlivsstatus : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE015.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { fieldDefinition.codeExists(it[PRIMK_SIVILS_A_COL_NAME]) }
        .map {
            createValidationReportEntry(
                messageText = "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller feil kode " +
                        "er benyttet. Fant '${it[PRIMK_SIVILS_A_COL_NAME]}', forventet én av: ${fieldDefinition.codeList}. " +
                        "Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_A_COL_NAME],
                journalId = it[JOURNAL_NR_A_COL_NAME]
            )
        }.ifEmpty { null }

    companion object {
        private val fieldDefinition = fieldDefinitions.byColumnName(PRIMK_SIVILS_A_COL_NAME)
    }
}