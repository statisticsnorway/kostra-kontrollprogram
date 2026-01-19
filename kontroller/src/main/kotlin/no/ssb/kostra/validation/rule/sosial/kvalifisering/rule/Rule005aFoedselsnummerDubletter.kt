package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.util.SsnValidationUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule005aFoedselsnummerDubletter : AbstractRule<List<KostraRecord>>(
    SosialRuleId.FODSELSNUMMER_DUBLETTER_05A.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .asSequence()
        .filter { kostraRecord ->
            kostraRecord[KOMMUNE_NR_COL_NAME] != OSLO_MUNICIPALITY_ID
                    && SsnValidationUtils.isValidSocialSecurityIdOrDnr(kostraRecord[PERSON_FODSELSNR_COL_NAME], arguments.aargang.toInt())
        }
        .groupBy { kostraRecord -> kostraRecord[PERSON_FODSELSNR_COL_NAME] + kostraRecord[STATUS_COL_NAME] }
        .filter { (_, group) -> group.size > 1 }
        .toList()
        .flatMap { (_, group) ->
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
                    caseworker = kostraRecord[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                    journalId = journalId,
                )
            }
        }
        .toList().ifEmpty { null }
}
