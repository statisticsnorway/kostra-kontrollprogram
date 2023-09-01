package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.STRUKTUR_GR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule009Malgruppe : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52bRuleId.FAMILIEVERN52B_RULE009.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { fieldDefinition.codeExists(it[STRUKTUR_GR_B_COL_NAME]) }
        .map {
            createValidationReportEntry(
                messageText = "Det er ikke fylt ut hva som er målgruppe for behandlingen. " +
                        "Fant '${it[STRUKTUR_GR_B_COL_NAME]}', forventet én av: ${fieldDefinition.codeList}.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_B_COL_NAME],
                journalId = it[GRUPPE_NR_B_COL_NAME]
            )
        }.ifEmpty { null }

    companion object {
        private val fieldDefinition = fieldDefinitions.byColumnName(STRUKTUR_GR_B_COL_NAME)
    }
}