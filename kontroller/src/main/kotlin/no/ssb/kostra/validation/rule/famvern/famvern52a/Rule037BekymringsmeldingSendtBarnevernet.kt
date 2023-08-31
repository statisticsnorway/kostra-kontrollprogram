package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.BEKYMR_MELD_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule037BekymringsmeldingSendtBarnevernet : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE037.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context.filterNot {
        fieldDefinitions.byColumnName(BEKYMR_MELD_A_COL_NAME).codeExists(it[BEKYMR_MELD_A_COL_NAME])
    }.map {
        val codeList =
            fieldDefinitions.byColumnName(BEKYMR_MELD_A_COL_NAME).codeList
        createValidationReportEntry(
            messageText = "Det er ikke svart på hvorvidt bekymringsmelding er sendt barnevernet " +
                    "eller ei, eller feil kode er benyttet. Fant '${it[BEKYMR_MELD_A_COL_NAME]}', " +
                    "forventet én av: $codeList. Feltet er obligatorisk å fylle ut.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}