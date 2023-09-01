package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule005bJournalnummerDubletter : AbstractNoArgsRule<List<KostraRecord>>(
    SosialRuleId.JOURNALNUMMER_DUBLETTER_05B.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .groupBy { kostraRecord -> kostraRecord[PERSON_JOURNALNR_COL_NAME] }
        .filter { (_, group) -> group.size > 1 }
        .flatMap { (journalId, group) ->
            group.map { kostraRecord ->
                createValidationReportEntry(
                    "Journalnummer $journalId forekommer ${group.size} ganger.",
                    lineNumbers = listOf(kostraRecord.lineNumber)
                ).copy(
                    caseworker = kostraRecord[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                    journalId = journalId,
                )
            }
        }.ifEmpty { null }
}