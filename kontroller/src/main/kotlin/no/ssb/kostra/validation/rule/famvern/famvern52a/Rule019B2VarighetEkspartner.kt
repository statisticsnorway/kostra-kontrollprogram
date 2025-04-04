package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.EKSPART_VARIGH_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.PRIMK_VSRELASJ_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule019B2VarighetEkspartner :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE019B2.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filter { it[PRIMK_VSRELASJ_A_COL_NAME] == "2" }
            .filterNot { fieldDefinition.codeExists(it[EKSPART_VARIGH_A_COL_NAME]) }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Det er oppgitt at primærklientens relasjon til viktigste deltager er ekspartner, men det " +
                            "er ikke oppgitt hvor lenge partene var gift, samboere eller registrerte partnere. " +
                            "Fant '${it[EKSPART_VARIGH_A_COL_NAME]}', forventet én av: ${fieldDefinition.codeList}.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }

    companion object {
        private val fieldDefinition =
            fieldDefinitions.byColumnName(EKSPART_VARIGH_A_COL_NAME)
    }
}
