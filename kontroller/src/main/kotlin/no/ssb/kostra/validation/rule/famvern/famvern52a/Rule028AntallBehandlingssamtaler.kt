package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.ANTSAMT_IARET_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule028AntallBehandlingssamtaler : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE028.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        0 < it.fieldAsIntOrDefault(ANTSAMT_IARET_A_COL_NAME)
    }.map {
        createValidationReportEntry(
            messageText = "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført " +
                    "i saken i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}