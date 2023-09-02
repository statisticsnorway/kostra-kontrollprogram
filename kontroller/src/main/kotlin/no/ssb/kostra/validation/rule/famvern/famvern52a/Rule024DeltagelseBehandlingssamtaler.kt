package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_ANDR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_BARNO18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_BARNU18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_EKSPART_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_FORELDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_OVRFAM_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_PARTNER_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_VENN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule024DeltagelseBehandlingssamtaler : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE024.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { deltakere.any { deltaker -> fieldDefinitions.byColumnName(deltaker).codeExists(it[deltaker]) } }
        .map {
            createValidationReportEntry(
                messageText = "Det er ikke krysset av for om andre deltakere i saken har deltatt i samtaler " +
                        "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_A_COL_NAME],
                journalId = it[JOURNAL_NR_A_COL_NAME]
            )
        }.ifEmpty { null }

    companion object {
        private val deltakere = listOf(
            DELT_PARTNER_A_COL_NAME,
            DELT_EKSPART_A_COL_NAME,
            DELT_BARNU18_A_COL_NAME,
            DELT_BARNO18_A_COL_NAME,
            DELT_FORELDRE_A_COL_NAME,
            DELT_OVRFAM_A_COL_NAME,
            DELT_VENN_A_COL_NAME,
            DELT_ANDR_A_COL_NAME,
        )
    }
}