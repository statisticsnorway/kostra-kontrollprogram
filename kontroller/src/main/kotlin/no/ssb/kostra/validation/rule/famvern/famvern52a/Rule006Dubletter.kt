package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule


class Rule006Dubletter : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE006.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .groupBy { it[KONTOR_NR_A_COL_NAME] to it[JOURNAL_NR_A_COL_NAME] }.filter { (_, group) -> group.size > 1 }
        .flatMap { (kontorJournalNummerPair, group) ->
            createSingleReportEntryList(
                "Journalnummeret er benyttet på mer enn en sak (${group.size} stk). " +
                        "Dubletter på kontornummer '${kontorJournalNummerPair.first}' - " +
                        "journalnummer '${kontorJournalNummerPair.second}'.",
                lineNumbers = group.map { it.lineNumber }
            )
        }.ifEmpty { null }
}