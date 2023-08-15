package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.HOVEDI_GR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule010GruppensHovedtema : AbstractRule<List<KostraRecord>>(
    Familievern52bRuleId.FAMILIEVERN52B_RULE010.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        fieldDefinitions.byColumnName(HOVEDI_GR_B_COL_NAME).codeExists(it[HOVEDI_GR_B_COL_NAME])
    }.map {
        val codeList =
            fieldDefinitions.byColumnName(HOVEDI_GR_B_COL_NAME).codeList
        createValidationReportEntry(
            messageText = "Det er ikke fylt ut hva som er målgruppe for behandlingen. " +
                    "Fant '${it[HOVEDI_GR_B_COL_NAME]}', forventet én av: $codeList.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_B_COL_NAME],
            journalId = it[GRUPPE_NR_B_COL_NAME]
        )
    }.ifEmpty { null }
}