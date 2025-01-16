package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule006Dubletter :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52bRuleId.FAMILIEVERN52B_RULE006.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .groupBy { it[KONTOR_NR_B_COL_NAME] to it[GRUPPE_NR_B_COL_NAME] }
            .filter { (_, group) -> group.size > 1 }
            .flatMap { (kontorGruppeNummerPair, group) ->
                createSingleReportEntryList(
                    "Gruppenummeret er benyttet på mer enn en sak (${group.size} stk). " +
                        "Dubletter på kontornummer '${kontorGruppeNummerPair.first}' - " +
                        "gruppenummer '${kontorGruppeNummerPair.second}'.",
                    lineNumbers = group.map { it.lineNumber },
                )
            }.ifEmpty { null }
}
