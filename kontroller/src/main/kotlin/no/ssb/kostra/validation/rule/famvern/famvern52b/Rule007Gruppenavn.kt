package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NAVN_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule007Gruppenavn : AbstractRule<List<KostraRecord>>(
    Familievern52bRuleId.FAMILIEVERN52B_RULE007.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        it[GRUPPE_NAVN_B_COL_NAME].isNotBlank()
    }.map {
        createValidationReportEntry(
            messageText = "Det er ikke oppgitt navn på gruppen. Tekstfeltet skal ha maksimalt 30 posisjoner.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_B_COL_NAME],
            journalId = it[GRUPPE_NR_B_COL_NAME]
        )
    }.ifEmpty { null }
}