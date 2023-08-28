package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.EKSPART_LENGDE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.PRIMK_VSRELASJ_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule019B1VarighetSidenBrudd : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE019B1.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filter {
        it[PRIMK_VSRELASJ_A_COL_NAME] == "2"
    }.filterNot {
        fieldDefinitions.byColumnName(EKSPART_LENGDE_A_COL_NAME).codeExists(it[EKSPART_LENGDE_A_COL_NAME])
    }.map {
        val codeList =
            fieldDefinitions.byColumnName(EKSPART_LENGDE_A_COL_NAME).codeList
        createValidationReportEntry(
            messageText = "Det er oppgitt at primærklientens relasjon til viktigste deltager er ekspartner, men det " +
                    "er ikke oppgitt tid siden brudd. Fant '${it[EKSPART_LENGDE_A_COL_NAME]}', " +
                    "forventet én av: $codeList.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}