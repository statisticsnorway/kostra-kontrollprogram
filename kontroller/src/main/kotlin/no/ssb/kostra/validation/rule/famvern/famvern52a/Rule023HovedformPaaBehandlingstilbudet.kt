package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.HOVEDF_BEHAND_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule023HovedformPaaBehandlingstilbudet : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE023.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        fieldDefinitions.byColumnName(HOVEDF_BEHAND_A_COL_NAME).codeExists(it[HOVEDF_BEHAND_A_COL_NAME])
    }.map {
        val codeList =
            fieldDefinitions.byColumnName(HOVEDF_BEHAND_A_COL_NAME).codeList
        createValidationReportEntry(
            messageText = "Det er ikke krysset av for hva som har vært hovedformen på behandlingstilbudet siden " +
                    "saken ble opprettet, eller feil kode er benyttet. Fant '${it[HOVEDF_BEHAND_A_COL_NAME]}', " +
                    "forventet én av: $codeList. Feltet er obligatorisk å fylle ut.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}