package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule005aFoedselsnummerDubletter : AbstractNoArgsRule<List<KostraRecord>>(
    SosialRuleId.FODSELSNUMMER_DUBLETTER_05A.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { record ->
            isValidSocialSecurityIdOrDnr(record[PERSON_FODSELSNR_COL_NAME])
        }.groupBy { kostraRecord ->
            kostraRecord[PERSON_FODSELSNR_COL_NAME]
        }.filter { (_, group) ->
            group.size > 1
        }.flatMap { (_, group) ->
            group.map { kostraRecord ->
                val journalId = kostraRecord[PERSON_JOURNALNR_COL_NAME]

                val otherJournalIds = group
                    .map { innerRecord -> innerRecord[PERSON_JOURNALNR_COL_NAME] }
                    .filter { innerJournalId -> innerJournalId != journalId }
                    .joinToString(", ")

                createValidationReportEntry(
                    "Fødselsnummeret i journalnummer $journalId fins også i journalene $otherJournalIds.",
                    lineNumbers = listOf(kostraRecord.lineNumber)
                ).copy(
                    caseworker = kostraRecord[SAKSBEHANDLER_COL_NAME],
                    journalId = journalId,
                )
            }
        }.ifEmpty { null }
}
