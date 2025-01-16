package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TIMER_IARET_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule030TotaltAntallTimer :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE030.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot { 0 < it.fieldAsIntOrDefault(TIMER_IARET_A_COL_NAME) }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre " +
                            "ved kontoret har anvendt på saken (timer benyttet til gruppesamtaler skal holdes utenfor) " +
                            "i løpet av året (for og etterarbeid skal ikke regnes med). " +
                            "Feltet er obligatorisk å fylle ut.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }
}
