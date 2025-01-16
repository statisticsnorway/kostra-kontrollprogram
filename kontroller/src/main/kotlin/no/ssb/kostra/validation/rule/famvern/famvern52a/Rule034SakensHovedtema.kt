package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.HOVEDTEMA_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule034SakensHovedtema :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE034.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filter { it[STATUS_ARETSSL_A_COL_NAME] in listOf("1", "2") }
            .filterNot { fieldDefinition.codeExists(it[HOVEDTEMA_A_COL_NAME]) }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Det er krysset av for at saken er avsluttet i rapporteringsåret, men " +
                            "ikke fylt ut hovedtema for saken, eller feltet har ugyldig format. " +
                            "Fant '${it[HOVEDTEMA_A_COL_NAME]}', forventet én av: ${fieldDefinition.codeList}.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }

    companion object {
        private val fieldDefinition =
            fieldDefinitions.byColumnName(HOVEDTEMA_A_COL_NAME)
    }
}
