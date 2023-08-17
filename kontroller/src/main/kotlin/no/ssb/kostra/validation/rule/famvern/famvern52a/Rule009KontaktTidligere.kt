package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTAKT_TIDL_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule009KontaktTidligere : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE009.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        fieldDefinitions.byColumnName(KONTAKT_TIDL_A_COL_NAME).codeExists(it[KONTAKT_TIDL_A_COL_NAME])
    }.map {
        val codeList = fieldDefinitions.byColumnName(KONTAKT_TIDL_A_COL_NAME).codeList
        createValidationReportEntry(
            messageText = "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved familievernet " +
                    "tidligere, eller feil kode er benyttet. Fant '${it[KONTAKT_TIDL_A_COL_NAME]}', " +
                    "forventet én av: $codeList. Feltet er obligatorisk å fylle ut.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}