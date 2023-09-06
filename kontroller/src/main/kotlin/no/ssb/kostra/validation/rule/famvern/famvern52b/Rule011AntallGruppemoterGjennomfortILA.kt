package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.ANTMOTERTOT_IARET_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule011AntallGruppemoterGjennomfortILA : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52bRuleId.FAMILIEVERN52B_RULE011.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { 0 < it.fieldAsIntOrDefault(ANTMOTERTOT_IARET_B_COL_NAME) }
        .map {
            createValidationReportEntry(
                messageText = "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt " +
                        "i løpet av rapporteringsåret.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_B_COL_NAME],
                journalId = it[GRUPPE_NR_B_COL_NAME]
            )
        }.ifEmpty { null }
}