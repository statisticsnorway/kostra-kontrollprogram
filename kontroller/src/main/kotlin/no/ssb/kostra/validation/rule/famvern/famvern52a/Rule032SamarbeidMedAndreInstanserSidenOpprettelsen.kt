package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_ANDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_HELSE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_INGEN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_JURIST_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_KOMMB_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_KRISES_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_LEGE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_PSYKH_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_SKOLE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_SOS_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMARB_STATB_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule032SamarbeidMedAndreInstanserSidenOpprettelsen :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE032.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot {
                fields.any { field ->
                    fieldDefinitions.byColumnName(field).codeExists(it[field])
                }
            }.map {
                createValidationReportEntry(
                    messageText =
                        "Det er ikke krysset av for om det har vært samarbeid med andre " +
                            "instanser siden saken ble opprettet. Feltet er obligatorisk å fylle ut.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }

    companion object {
        private val fields =
            listOf(
                SAMARB_INGEN_A_COL_NAME,
                SAMARB_LEGE_A_COL_NAME,
                SAMARB_HELSE_A_COL_NAME,
                SAMARB_PSYKH_A_COL_NAME,
                SAMARB_JURIST_A_COL_NAME,
                SAMARB_KRISES_A_COL_NAME,
                SAMARB_SKOLE_A_COL_NAME,
                SAMARB_SOS_A_COL_NAME,
                SAMARB_KOMMB_A_COL_NAME,
                SAMARB_STATB_A_COL_NAME,
                SAMARB_ANDRE_A_COL_NAME,
            )
    }
}
